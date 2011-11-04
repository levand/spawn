(ns spawn.genome
  "Contains functions for converting genomes into projects"
  (:require [clostache.parser :as parser]
            [clojure.string :as str]
            [clojure.java.io :as io])
  (:import (java.io File StringWriter)))

(defn write-textfile
  "Writes a string as a textfile. Noop if the file already exists."
  [base name content]
  (let [path (str (.getCanonicalPath base) File/separator name)
        file (File. path)]
    (if (not (.exists file))
      (spit file content))))

(defn write-directory
  "Writes a directory. Noop if the directory already exists.
Returns a java.io.File representing the directory."
  [base name]
  (let [path (str (.getCanonicalPath base) File/separator name)
        file (File. path)]
    (if (not (.exists file))
      (.mkdir file))
    file))

(defn build-filesystem
  "Uses the provided map to create a filesystem structure.
Keys should be strings and represent file/dir names.
String values are saved as text files.
Map values are implied to be dirs and are written recursively."
  ([files]
     (build-filesystem files (File. ".")))
  ([files base]
     (let [base (if (instance? File base) base (File. base))]
       (doseq [name (keys files)]
         (let [content (files name)]
           (cond
            (string? content) (write-textfile base name content)
            (map? content) (build-filesystem content (write-directory base name))
            :else (throw (Exception. "build-filesystem only supports string and map values"))))))))

(defn- get-template [t]
  (let [writer (StringWriter.)]
    (io/copy (.getResourceAsStream (clojure.lang.RT/baseLoader) t) writer)
    (str writer)))

(defn apply-template
  "Applies a mustache template and returns the result as a string."
  [template data]
  (parser/render (get-template template) data))

(defn underscore
  "replace dashes with underscores"
  [name]
  (str/replace name \- \_))
