(ns browser.scratch)

(def mike js.window.location.href)

(def mark (.toLowerCase
           (.substring
            js.window.location.href
            (- js.window.location.href.length 3))))



