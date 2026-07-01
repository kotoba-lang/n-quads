(ns n-quads.core
  "N-Quads serializer for EDN RDF quads."
  (:require [clojure.string :as str]))

(defn esc [s]
  (-> (str s)
      (str/replace "\\" "\\\\")
      (str/replace "\"" "\\\"")
      (str/replace "\n" "\\n")
      (str/replace "\r" "\\r")))

(defn iri-ref [s] (str "<" s ">"))

(defn term [x]
  (case (:rdf/type x)
    :iri (iri-ref (:value x))
    :blank (str "_:" (:id x))
    :literal (str "\"" (esc (:value x)) "\""
                  (cond
                    (:language x) (str "@" (:language x))
                    (:datatype x) (str "^^" (iri-ref (:value (:datatype x))))
                    :else ""))
    (throw (ex-info "Unknown RDF term" {:term x}))))

(defn quad [{:keys [subject predicate object graph]}]
  (str (term subject) " " (term predicate) " " (term object)
       (when graph (str " " (term graph)))
       " ."))

(defn n-quads [quads]
  (str (str/join "\n" (map quad quads))
       (when (seq quads) "\n")))

(def render n-quads)
