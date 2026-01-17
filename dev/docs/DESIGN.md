# EDN Bindings for GCP java SDKs

##  AGENT ENVIRONMENT

+ `GOOGLEAPIS_REPOS_PATH` codegen requires local clones from https://github.com/googleapis/
+ `GCP_CACHE_PATH` cached class analysis
+ `GCP_REPO_ROOT`absolute path to base of repo so code can be running independently of repl runtime

---
## Bindings

There are individual clojure files that correspond to user-level classes in the corresponding java sdk. Each class
has a to-edn & from-edn function that roundtrips edn→instance→edn

+ __from-edn__ accepts a singular clojure value and creates an instance from it
  + `(defn ^<Class> from-edn [{:keys [...] :as arg}] ...)`
  + it is preferable to construct a builder and set values (as available)

+ __to-edn__ accepts an instance and converts it into an edn representation
  + `(defn to-edn [^<Class> arg] ...)`

+ Nested classes are given `<NestedClass>:from-edn` & `<NestedClass>:to-edn` local to their parent namespace

+ the bindings retain the sdk api version in both classpath, namespace, and keyword design

+ binding files *MUST REQUIRE THEIR DEPENDENTS*
  - the schema system is not for managing the classpath
  - `(:require [gcp.<package>.<version>...<Class> :as CapitalClass] ...)`

---
### Generation Provenance
Every generated binding file includes a `:gcp.dev/provenance` map in its namespace metadata. This map provides a cryptographic signature of the inputs used to generate the file, enabling deterministic builds and incremental regeneration.

```clojure
(ns gcp.vertexai.v1.api.GenerateContentRequest
  {:gcp.dev/provenance
   {:signature "..."               ;; Composite hash of Source + Toolchain + AST
    :source    {:package "..." :version "..." :class "..." :git-sha "..."}
    :toolchain-hash "..."          ;; Hash of the gcp.dev source code
    :generation-time "..."}
   :gcp.dev/certification
   {:protocol-hash "..."           ;; Hash of the certification protocol definition
    :base-seed 12345               ;; Starting seed for fuzzing
    :timestamp "..."
    :passed-stages {:smoke 12345, :standard 12346, :stress 12347}}}
  ...)
```

---
### Binding Certification Protocol
Binding quality is enforced through a formalized 3-stage fuzzing protocol. The protocol configuration itself is hashed (`:protocol-hash`) to prevent silent changes in verification rigor.

*   **Smoke (100 tests, size 10):** Rapid verification of basic construction and roundtripping.
*   **Standard (100 tests, size 30):** Deeper exploration of field combinations and nested structures.
*   **Stress (100 tests, size 50):** Large-scale data generation to catch edge cases in collection normalization and type constraints.

Certification is **sticky**: files are only regenerated if the provenance (source code or toolchain) or the certification protocol changes.

---
## `GCP_REPO_ROOT/packages/global` := `gcp.global`

+ packages/global manages a shared malli registry for all packages
  - its is ok to instrument global or modify it to improve error reporting or sensible defaults
  - other packages need to use this, not ok to make changes to this without considering other users

+ keys for schemas are given form `:gpc.<package>.<api/version>?(<.component>+)/<Class>`
  - gcp.global will not accept simple keys on registration

+ schemas themselves can reference other schemas by their keys, including themselves

+ `(gcp.global/instance-schema <FQCN>)` should be used to accept pass through instances while simultaneously lazily loading the class into the SCI environment for fuzzing

+ schemas must be edn-safe and are validated at registration time via sci. this gives upfront guarantee that relevant debug info can be serialization for use by LLMs
  + functions must be quoted
  + instance predicates should use `gcp.global/instance-schema` which will include the class for sci to validate

+ individual bindings register their own schema, a file with multiple bindings (rare!) can register together via map syntax

+ synthetic schemas fill semantic gaps
  + link user functions to the class schemas
  + provide structure to under-typed APIs (like semantics embedded in string formatting) 
  + mediate polymorphism and sugaring

+ `gcp.global/*strict?*` enforces schema validation for both data input to `from-edn` and recovered from `to-edn`

+ `gcp.gen` wraps `malli.generator` put uses the gcp.global malli api

---
## `GCP_REPO_ROOT/packages/foreign` := `gcp.foreign`

The `gcp.foreign` system provides a **systematic** mechanism for handling external Java dependencies (types not within the generated SDK package itself). It acts as a "clean slate" replacement for ad-hoc mappings.

### Systematic Mapping Rule
All foreign types are mapped to namespaces under `gcp.foreign` based on their Java package structure. These packages are provided transitively via the sdk jars themselves

**Rule:** `(symbol (str "gcp.foreign." <java-package>))`

| Java FQCN | Clojure Namespace | File Path |
| :--- | :--- | :--- |
| `com.google.protobuf.ByteString` | `gcp.foreign.com.google.protobuf` | `src/gcp/foreign/com/google/protobuf.clj` |
| `com.google.type.Date` | `gcp.foreign.com.google.type` | `src/gcp/foreign/com/google/type.clj` |
| `com.google.api.MonitoredResource` | `gcp.foreign.com.google.api` | `src/gcp/foreign/com/google/api.clj` |
| `com.google.api.gax.retrying.RetrySettings` | `gcp.foreign.com.google.api.gax.retrying` | `src/gcp/foreign/com/google/api/gax/retrying.clj` |

### Binding Conventions
Foreign binding files differ slightly from generated bindings:
*   **Systematic Function Names:** Conversion functions include the type name to avoid collisions within the package-level namespace.
    *   `ByteString-from-edn` / `ByteString-to-edn`
    *   `Status-from-edn` / `Status-to-edn`
*   **Multiple Bindings:** A single file (e.g., `com/google/api.clj`) may contain bindings for multiple classes from that package (e.g., `MonitoredResource`, `HttpBody`).

### Standard Library Support
While `gcp.foreign` is primarily for external dependencies, we explicitly bind and certify Java Standard Library types that appear in the API surface (e.g., `java.io.InputStream`, `java.time.Duration`) to ensure the fuzzing infrastructure generates valid inputs for them.
*   **Raw Containers**: Raw `java.util.List` and `java.util.Map` are exempted from foreign certification (treated as native) because they map directly to Clojure persistent collections.

### Toolchain Integration
The toolchain (`gcp.dev`) automatically resolves foreign dependencies using this system:
*   **`infer-foreign-ns`**: Calculates the target namespace from the FQCN.
*   **`foreign-binding-exists?`**: Checks if the target binding file actually exists.
*   **Emission**:
    *   If the binding exists **and** exports the systematic conversion functions: Emits `(:require [gcp.foreign... :as ...])` and calls them.
    *   If the binding namespace is missing, the specific function DNE, or it lacks fuzz certification: **Throws an exception**

---
### Foreign Certification Protocol
External Java dependencies (types in `gcp.foreign`) require manual binding implementation but are subject to the same rigorous fuzzing standards as generated bindings.

*   **Systematic Discovery**: The `certify-foreign-namespace` tool scans a target namespace for `TypeName-from/to-edn` pairs and their corresponding Malli schemas (supporting recursive schemas defined in local `registry` maps).
*   **Source-Aware Hashing**: The `:protocol-hash` for foreign bindings is a composite hash of the certification protocol version **and** the source code of the fuzzing toolchain (`gcp.dev.toolchain.fuzz`). This ensures that logic changes in the verification engine invalidate previous certifications.
*   **Enforcement**: The toolchain emitter (`gcp.dev.toolchain.emitter`) performs a mandatory certification check for every foreign namespace reference. If a namespace lacks valid `:gcp.dev/certification` metadata, compilation is aborted.
*   **Formatting**: Certification results are injected into the namespace metadata using structural editing (`rewrite-clj`) and formatted via `zprint` to maintain high readability in source control.
*   **Idempotency**: To allow embedding certification results directly in the source file without invalidating its own source hash, the source hashing logic (`compute-foreign-source-hash`) specifically parses the file and excludes the `:gcp.dev/certification` metadata key before computing the checksum.

**Certification Workflow:**
1.  **Identify Missing Bindings**: Use `gcp.dev.build/check-package-foreign` to discover uncertified dependencies used by the SDKs.
2.  **Implement Bindings**: Create the corresponding `gcp.foreign.package.namespace` file. Define `Type-from-edn`, `Type-to-edn`, and register the schema.
    *   *Standard Library Exception*: Raw container types (`java.util.List`, `java.util.Map`) and primitives are handled natively by the toolchain/host and do not require foreign bindings.
3.  **Certify**: Run `gcp.dev.toolchain.fuzz/certify-foreign-namespace`. This validates the bindings using the fuzzing protocol and returns the certification result.
4.  **Inject Metadata**: Pass the result to `gcp.dev.toolchain.fuzz/update-foreign-namespace-certification`. This updates the source file in-place with the cryptographic proof of certification.


---
## `GCP_REPO_ROOT/dev` := `gcp.dev`

The generation pipeline in `gcp.dev.toolchain`, consists of:
*   `gcp.dev.toolchain.parser`: Orchestrates Java source parsing with Git-aware caching.
*   `gcp.dev.toolchain.analyzer`: Converts raw Java AST into structured EDN nodes.
*   `gcp.dev.toolchain.malli`: Maps Java types to Malli schemas with numeric constraints.
*   `gcp.dev.toolchain.emitter`: Low-level Clojure code generator (supports return/arg type hints).
*   `gcp.dev.compiler`: High-level orchestrator that manages the build process and provenance.

#### `gcp.dev.toolchain.parser`
Orchestrates Java source parsing with Git-aware caching. It uses `javaparser` to read source files and extract a raw AST. It handles:
*   **Analysis**: Accepts a list of files (from `gcp.dev.packages`) and produces a structured AST.
*   **Caching**: Uses a composite hash of the toolchain source, git revision, and **input file list** to ensure the cache is invalidated when inclusion rules change.
*   File discovery (including `package-info.java`).
*   Incremental parsing via SHA-256 caching of input files.
*   Git integration to extract commit SHAs for provenance.

**User Classes Definition:**
Given a java package directory, the parser crawls **user-classes** from java source code and produces maps describing their content.
*   **User-classes** are public classes that configure a client, interact with client methods, or can be potential return types.
*   **NOT user-classes** (even if public): SPIs, internal classes, annotations, testing utilities, int structs, etc.

#### `gcp.dev.toolchain.analyzer`
Converts the raw Java AST nodes into a rich, structured Intermediate Representation (IR) suitable for code generation. It determines the **category** of each class, which is used to select the schema and binding emission strategy.

**Categories:**
*   Categories are used to select schema and binding emission strategies.

It also:
*   Resolves type dependencies.
*   Normalizes method names to property names.
*   **Foreign Resolution:** Identifies external dependencies and verifies their existence in `gcp.foreign`.

#### `gcp.dev.toolchain.malli`
Generates Malli schemas from the analyzed IR nodes. It maps Java types to Clojure/Malli equivalents, handling:
*   Primitive types (int, float, boolean).
*   Collections (List, Map).
*   **Systematic Foreign Mapping:** Maps external types to their corresponding `gcp.foreign` schema keys (e.g., `:gcp.foreign.com.google.protobuf/ByteString`).
*   Numeric constraints (e.g., 32-bit bounds for `int` and `float`).

#### `gcp.dev.toolchain.emitter`
The low-level code generator. It accepts an analyzed node and metadata, producing the final Clojure source code (forms). It handles:
*   Namespace declaration with injected metadata (provenance + certification).
*   `from-edn` and `to-edn` function generation with type hints for performance.
*   Schema definition and registration forms.
*   **Foreign Integration:** Generates requires and conversion calls using the systematic `gcp.foreign` naming convention (`Namespace/Type-from-edn`).
*   **Certification Enforcement:** Validates that all required `gcp.foreign` namespaces have a valid `:gcp.dev/certification` metadata map before emission, throwing an exception if not.
*   **Filtering:** Automatically excludes `:builder` types from binding and schema emission, focusing only on user-facing accessor types.

### Orchestration

#### `gcp.dev.compiler`
The top-level orchestrator. It ties the toolchain components together to produce artifacts.
*   **Provenance Calculation:** Uses `gcp.dev.digest` to calculate the "Generation Signature".
*   **Signature Injection:** Passes the signature and provenance data to the emitter.
*   **Certification Orchestration:** Executes the V1 Certification Protocol before writing artifacts to disk.
*   **Compilation:** Delegates the actual code generation to `toolchain.emitter`.

#### `gcp.dev.digest`
Implements the hashing logic for provenance.
*   Calculates SHA-256 hashes of source files in `gcp.dev`.
*   Computes the "Toolchain Hash" (state of the compiler itself).
*   Computes the "Protocol Hash" (rigor of the certification process).
*   Computes the final "Binding Hash" (Source + Toolchain + AST).

#### `gcp.dev.packages`
High-level API for querying the Java SDKs.
*   **Static Configuration**: Packages are defined with static maps specifying:
    *   `:root`: Base directory of the SDK.
    *   `:include`: Vector of file or directory paths (relative to root) to analyze.
    *   `:exclude`: Vector of paths to ignore.
    *   `:native-prefixes`: Set of package prefixes considered "internal" (e.g., `com.google.cloud.storage` AND `com.google.storage` for the Storage SDK).
*   **File Resolution**: The `resolve-files` function interprets the static config to produce a concrete list of Java files, which is passed to the parser.
*   **Polymorphism**: The `parse` function (and downstream API) accepts either a **Package Keyword** (e.g., `:vertexai`), a **Static Map**, or a pre-calculated **Parsed Map**.
*   **`package-api-types`**: Returns the sorted list of all binding targets (FQCNs) for a package.

#### `gcp.dev.packages.maven`
Provides Maven artifact resolution capabilities using `clojure.tools.deps.util.maven`.
*   **Version Resolution:** Fetches latest stable and candidate versions for artifacts.
*   **Aether Integration:** Wraps the Eclipse Aether library for robust dependency management.

#### `gcp.dev.packages.git`
Provides a functional wrapper around the Git CLI, inspired by `clojure.tools.gitlibs`.
*   **Porcelain Commands:** Implements `fetch`, `tags`, `checkout`, `create-worktree`, `remove-worktree`.
*   **Worktree Management:** Safe handling of detached worktrees for specific tags/commits.

#### `gcp.dev.fuzz`
Automated verification system.
*   **Roundtrip Fuzzing**: Generates random EDN data based on the schema, converts it to a Java object, converts it back to EDN, and verifies equality/validity.
*   **Multi-stage Protocol**: Implements the Smoke, Standard, and Stress testing stages used during certification.
*   **`certify-foreign-namespace`**: Automates the discovery and fuzz-testing of manual bindings in `gcp.foreign` namespaces.
*   **`update-foreign-namespace-certification`**: Injects certification proofs directly into the source file metadata, enabling idempotent and verifiable builds.


---
## COMMANDS


### Development Workflow (Safe Reloading)
```clojure
(require 'gcp.dev.repl)
(gcp.dev.repl/refresh)
```

### Analyze a class

```clojure
(require '[gcp.dev.toolchain.analyzer :as ana])
(require '[gcp.dev.packages :as pkg])
(ana/analyze-class :vertexai "com.google.cloud.vertexai.api.GenerateContentRequest")
```

### Compile and Certify
The standard way to generate or update a certified binding file:

```clojure
(require '[gcp.dev.compiler :as c])
(c/compile-and-certify :vertexai 
                       "com.google.cloud.vertexai.api.GenerateContentRequest" 
                       "GenerateContentRequest.clj" 
                       {:seed 12345})
```

### Running the Fuzzer
The primary command used to verify a class and its dependencies:

```clojure
(require 'gcp.dev.toolchain.fuzz) 
(gcp.dev.toolchain.fuzz/certify-class :vertexai "com.google.cloud.vertexai.api.GenerateContentRequest" {:num-tests 100 :timeout-ms 60000})
```

### Inspecting Generated Code
To debug or inspect the raw Clojure code generated for a specific class:

```clojure
(require :reload '[gcp.dev.packages :as pkg] 
                 '[gcp.dev.toolchain.analyzer :as ana] 
                 '[gcp.dev.compiler :as c]) 
(let [node (pkg/lookup-class :vertexai "com.google.cloud.vertexai.api.GenerateContentRequest") 
      ana-node (ana/analyze-class-node node)]
  (c/compile-class ana-node))
```

### Check Foreign Dependencies
Audit the codebase for uncertified external dependencies:

```clojure
(require 'gcp.dev.sync)
(gcp.dev.sync/check-foreign)
```

### Certify Foreign Namespace
Certify and update a manual binding file in `gcp.foreign`:

```clojure
(require 'gcp.dev.toolchain.fuzz)
(let [results (gcp.dev.toolchain.fuzz/certify-foreign-namespace 'gcp.foreign.java.util {})]
  (gcp.dev.toolchain.fuzz/update-foreign-namespace-certification 'gcp.foreign.java.util results))
```

### Clearing Cache
If definitions or categorization rules change:

```clojure
(require :reload '[gcp.dev.toolchain.parser :as p])
(p/clear-cache)
```

### Building a Package
```clojure
(do 
  (require '[gcp.dev.build] :reload) 
  (gcp.dev.build/build-package :bigquery))
```

---
## TESTS
 + `gcp/dev/test/gcp/dev/test/` holds codegen tests
 + `gcp/dev/test/gcp/dev/test_runner.clj` requires executes the full suite

### run tests via repl

```clojure
;; run one from repl
(require :reload 'clojure.test 'gcp.dev.test.toolchain.analyzer-test)
(clojure.test/run-tests 'gcp.dev.test.toolchain.analyzer-test)

;; run all from repl
(require '[gcp.dev.test-runner])
(gcp.dev.test-runner/run-all-tests)
```

### run tests via exec

```bash
## from gcp/dev
clj -M -m gcp.dev.test-runner
```

### Key Test Namespaces

*   **`gcp.dev.test.packages-test`**:
    *   Verifies class lookup, user type extraction, and foreign dependency detection.
    *   Ensures foreign dependencies are correctly identified and do not leak internal types.

*   **`gcp.dev.test.toolchain.analyzer-test`**:
    *   Smoke tests the analyzer against all reachable classes in the SDKs.

*   **`gcp.dev.test.toolchain.emitter-test`**:
    *   Unit tests for low-level emission logic (function generation, type hints).

*   **`gcp.dev.test.toolchain.emitter-foreign-handling-test`**:
  *   Verifies that the emitter strictly enforces certification for foreign dependencies.
  *   Tests that uncertified namespaces throw compilation errors.
  *   **Hashing Consistency**: Explicitly verifies that a file's source hash remains identical whether or not it contains certification metadata.

---
## PACKAGES

### packages/vertexai
+ LOAD_TEST_COMMAND  : `clojure -A:gcp/test -M -e "(require 'gcp.vertexai.load-test)"`
+ LOCAL_JAVA_SDK_REPO: GOOGLEAPIS_REPOS_PATH/google-cloud-java/java-vertexai
+ STATUS
    + [] complete bindings
    + [] fuzzed
    + [] attestation
    + [] DWIM api

### packages/storage
+ LOAD_TEST_COMMAND : `clojure -A:gcp/test -M -e "(require 'gcp.storage.load-test)"`
+ LOCAL_JAVA_SDK_REPO: GOOGLEAPIS_REPOS_PATH/java-storage
+ STATUS
    + [] complete bindings
    + [] fuzzed
    + [] attestation
    + [] DWIM api

### packages/storage-control
+ LOAD_TEST_COMMAND : `clojure -A:gcp/test -M -e "(require 'gcp.storage-control.load-test)"`
+ LOCAL_JAVA_SDK_REPO: GOOGLEAPIS_REPOS_PATH/java-storage
+ STATUS
  + [] complete bindings
  + [] fuzzed
  + [] attestation
  + [] DWIM api

### packages/bigquery
+ LOAD_TEST_COMMAND  : `clojure -A:gcp/test -M -e "(require 'gcp.bigquery.load-test)"`
+ LOCAL_JAVA_SDK_REPO: GOOGLEAPIS_REPOS_PATH/java-bigquery
+ STATUS
    + [~] complete bindings
    + [] fuzzed
    + [] attestation
    + [] DWIM api

### packages/pubsub
+ LOAD_TEST_COMMAND  : `clojure -A:gcp/test -M -e "(require 'gcp.pubsub.load-test)"`
+ LOCAL_JAVA_SDK_REPO: GOOGLEAPIS_REPOS_PATH/java-pubsub
+ STATUS
    + [] complete bindings
    + [] fuzzed
    + [] attestation
    + [] DWIM api

### packages/logging
+ LOAD_TEST_COMMAND  : `clojure -A:gcp/test -M -e "(require 'gcp.logging.load-test)"`
+ LOCAL_JAVA_SDK_REPO: GOOGLEAPIS_REPOS_PATH/java-logging
+ STATUS
    + [] complete bindings
    + [] fuzzed
    + [] attestation
    + [] DWIM api

### packages/monitoring
+ LOAD_TEST_COMMAND  : `clojure -A:gcp/test -M -e "(require 'gcp.monitoring.load-test)"`
+ LOCAL_JAVA_SDK_REPO: GOOGLEAPIS_REPOS_PATH/google-cloud-java/java-monitoring
+ STATUS
    + [] complete bindings
    + [] fuzzed
    + [] attestation
    + [] DWIM api

### packages/genai
+ LOAD_TEST_COMMAND  : `clojure -A:gcp/test -M -e "(require 'gcp.genai.load-test)"`
+ LOCAL_JAVA_SDK_REPO: GOOGLEAPIS_REPOS_PATH/java-genai
+ STATUS
    + [] complete bindings
    + [] repl check
    + [] fuzzed
    + [] attestation

### packages/artifactregistry
+ LOAD_TEST_COMMAND  : `clojure -A:gcp/test -M -e "(require 'gcp.artifactregistry.load-test)"`
+ LOCAL_JAVA_SDK_REPO: GOOGLEAPIS_REPOS_PATH/google-cloud-java/java-artifact-registry
+ STATUS
    + [] complete bindings
    + [] repl check
    + [] fuzzed
    + [] attestation