(ns n-quads.core-test
  (:require [clojure.test :refer [deftest is]]
            [n-quads.core :as nq]))

(defn iri [v] {:rdf/type :iri :value v})
(defn lit [v] {:rdf/type :literal :value v})

(deftest renders-quads
  (let [q {:subject (iri "s") :predicate (iri "p") :object (lit "o") :graph (iri "g")}]
    (is (= "<s> <p> \"o\" <g> ." (nq/quad q)))
    (is (= "<s> <p> \"o\" <g> .\n" (nq/n-quads [q])))))
