(ns santas-little-helpers
  (:require [clojure.java.io :as io]))

(defn read-input
  "Opens a Java reader on provided input & returns a sequence
   separated by new-line"
  [input]
  (->> (io/resource input)
       (io/reader)
       (line-seq)))

(defn parse-int
  "Parses a Java character or string to an integer.
   Accepts an optional base argument for converting to
   a base n integer"
  ([s]
   (Integer/parseInt s))
  ([s base]
   (Integer/parseInt s base)))

(defn vec-remove
  "remove elem in coll"
  [pos coll]
  (vec (concat (subvec coll 0 pos) (subvec coll (inc pos)))))

(defn abs [n] (max n (- n)))
