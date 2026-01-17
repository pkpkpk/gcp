(ns gcp.dev.toolchain.shared)

#! IT IS FORBIDDEN TO MODIFY THIS SET WITHOUT ELICITING USER FOR APPROVAL
(def categories
  #{:abstract
    :abstract-union
    :accessor-with-builder
    :client
    :enum
    :error
    :exception
    :factory
    :functional-interface
    :interface
    :pojo
    :read-only
    :sentinel
    :static-factory
    :statics
    :string-enum
    :union-factory
    :variant-accessor

    ;; Nested Categories
    :nested/accessor-with-builder
    :nested/builder
    :nested/client
    :nested/enum
    :nested/factory
    :nested/string-enum
    :nested/abstract-union
    :nested/union-factory
    :nested/static-factory
    :nested/read-only
    :nested/pojo})
