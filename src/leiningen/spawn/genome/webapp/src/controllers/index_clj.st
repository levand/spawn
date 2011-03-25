(ns $name$.controllers.index
  (:use compojure.core))

(defroutes index-routes
  "Routes for the index controller"
  (GET "/" [] "Welcome from $name$!"))