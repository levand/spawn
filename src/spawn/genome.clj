(ns spawn.genome
  "Contains functions for converting genomes into projects"
  (:require [clostache.parser :as parser]
            [clojure.string :as str]
            [clojure.java.io :as io])
  (:import (java.io File StringWriter)))

(def ^{:dynamic true} *template-path* nil)

(defn write-textfile
  "Writes a string as a textfile. Noop if the file already exists."
  [base name content]
  (let [path (io/file (.getCanonicalPath base) name)]
    (if (not (.exists path))
      (spit path content))))

(defn write-directory
  "Writes a directory. Noop if the directory already exists.
   Returns a java.io.File representing the directory."
  [base name]
  (let [path (io/file (.getCanonicalPath base) name)]
    (if (not (.exists path))
      (.mkdir path))
    path))

(def ^{:dynamic true} *base* (io/file "."))

(defn build-filesystem
  "Uses the provided map to create a filesystem structure.
   Keys should be strings and represent file/dir names.
   String values are saved as text files.
   Map values are implied to be dirs and are written recursively."
  ([files]
     (build-filesystem files *base*))
  ([files base]
     (let [base (io/file base)]
       (doseq [name (keys files)]
         (let [content (files name)]
           (cond
            (string? content) (write-textfile base name content)
            (map? content) (build-filesystem content (write-directory base name))
            :else (throw (Exception. "build-filesystem only supports string and map values"))))))))

(defn- get-template [t]
  (let [writer (StringWriter.)]
    (io/copy (io/reader (io/resource (str t))) writer)
    (str writer)))

(defn render
  "Applies a mustache template and returns the result as a string.
   If *template-path* is not nil, assumes that template is a path
   relative to *template-path*. Otherwise, assumes template is the
   full path."
  [template data]
  (parser/render
   (get-template
    (if *template-path*
      (io/file *template-path* template)
      template))
   data))

(defmacro defgenome
  "Define a genome. Builds a genome function that calls build-filesystem on the
   genome map you pass with *template-path* bound to spawn/genome/<name-of-genome>."
  [name & [docs & more]]
  (let [[args body docs] (if (string? docs)
                                [(first more) (rest more) docs]
                                [docs more])]
    `(defn ~(if docs (with-meta name {:docs docs}) name) ~args
       (binding [*template-path* (io/file "spawn" "genome" ~(str name))]
         (build-filesystem
          ~@body)))))

(defn underscore
  "replace dashes with underscores"
  [name]
  (str/replace name \- \_))