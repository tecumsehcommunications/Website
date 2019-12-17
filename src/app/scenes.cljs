(ns app.scenes
     (:import goog.events))


(def loaded false)
  
(defn init [ sceneFile dispatcher ]
  
  (def loader (new js.THREE.GLTFLoader))
  
  (.load loader sceneFile 
    (fn [ gltf ]
      (def scene gltf.scene)
      (def camera (new js.THREE.PerspectiveCamera 45
                       (/ js.window.innerWidth js.window.innerHeight)
                       .25
                       20))
      
      (.forEach scene.children
           (fn [ el idx arr ]
             (if (= el.name "rSun")
               (aset el.children 0 "intensity" 3))
             (if (= el.name "lSun")
               (aset el.children 0 "intensity" 3))))
             
    
      (set! camera.far 10000)      
      (.set camera.position 1000 0 0)
      (.lookAt camera 0 0 0)
      (aset camera "aspect"
               (/ js.window.innerWidth js.window.innerHeight))
      (set! scene.aspect camera.aspect)
      (.updateProjectionMatrix camera)
      
       
      (set! scene.resize (fn []
            (aset camera "aspect"
               (/ js.window.innerWidth js.window.innerHeight))
            (if (> (. camera -aspect) (. scene -aspect))
                (set! (. camera -zoom) (. scene -aspect))
                (set! (. camera -zoom) (. camera -aspect)))
            (.updateProjectionMatrix camera)))

       
       (.push app.core.resizeArray scene)
  

       (set! scene.background
             (new js.THREE.Color 0xfefbd8))
       (set! loaded true)
       (.dispatchEvent dispatcher
             (new goog.events.Event "scene loaded")))))


