(ns $name$.server
  (:use compojure.core ring.adapter.jetty)
  (:require [compojure.route :as route]))

(defroutes main-routes
  (GET "/" [] "<h1>Hello from $name$</h1>")
  (route/files "/public" {:root "public"})
  (route/not-found "<h1>Page not found. Sorry.</h1>"))

(defn -main
  "Start up a basic Jetty server"
  [& args]
  (println "Starting Jetty on port 8080...")
  (run-jetty main-routes {:port 8080}))
