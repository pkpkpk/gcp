(ns gcp.dev.toolchain.shared)

#! IT IS FORBIDDEN TO MODIFY THIS SET WITHOUT ELICITING USER FOR APPROVAL
(def categories
  #{:accessor-with-builder
    :client
    :enum
    :error
    :exception
    :factory
    :functional-interface
    :interface
    :pojo
    :read-only
    :resource-extended
    :sentinel
    :static-factory
    :statics
    :string-enum
    :union-abstract
    :union-factory
    :union-concrete
    :variant-accessor

    :other
    :nested/other

    ;; Nested Categories
    :nested/accessor-with-builder
    :nested/builder
    :nested/client
    :nested/enum
    :nested/factory
    :nested/pojo
    :nested/read-only
    :nested/statics
    :nested/static-factory
    :nested/string-enum
    :nested/union-abstract
    :nested/union-factory
    })
