(ns app.scenes
     (:import goog.events))


(def loaded false)
  
(defn init [ sceneFile dispatcher ]
  
  (def loader (new js/THREE.GLTFLoader))
  
  (.load loader sceneFile 
      (fn [ gltf ]
      (def scene (new js/THREE.Scene))     
      (.add scene gltf.scene)
      (def camera (new js/THREE.PerspectiveCamera 45
                       (/ js/window.innerWidth js/window.innerHeight)
                       .25
                       20))
      (set! scene.objects (js-obj))
      
        (.forEach (aget app.scenes.scene.children 0 "children")
         (fn [ el idx arr ]
          (case el.name
            
             "lSun2" (aset el.children 0 "intensity" 1.5)
             "rSun2" (aset el.children 0 "intensity" 1.5)
             "bSun"  (aset el.children 0 "intensity" 3)
             "tSun"  (aset el.children 0 "intensity" 1)
             "fSun"  (aset el.children 0 "intensity" 3)

             "runners" (set! scene.objects.bottomRails el)
             "bottomRails" (set! scene.objects.bottomRails el)

             "gantry" (set! scene.objects.gantry el)
             "sideRails" (set! scene.objects.sideRails el)
             "stiffener" (set! scene.objects.stiffener el)
             "supportPole" (set! scene.objects.supportPole el)
              
             "bedRails" (set! scene.objects.bedRails el)
             "grate" (set! scene.objects.grate el)
             "truckBed" (set! scene.objects.truckBed el)
             "truckBody" (set! scene.objects.truckBody el)
             
              ())))
       
      (set! camera.far 10000)      
      (.set camera.position 1000 0 0)
      (.lookAt camera 0 0 0)
      (aset camera "aspect"
            (/ js/window.innerWidth js/window.innerHeight))
      (set! scene.aspect camera.aspect)
      (.updateProjectionMatrix camera)
      
       
      (set! scene.resize (fn []
            (aset camera "aspect"
               (/ js/window.innerWidth js/window.innerHeight))
            (if (> (. camera -aspect) (. scene -aspect))
                (set! (. camera -zoom) (. scene -aspect))
                (set! (. camera -zoom) (. camera -aspect)))
            (.updateProjectionMatrix camera)))

       
       (.push app.core.resizeArray scene)
  
      
       (set! loaded true)
       (.dispatchEvent dispatcher
             (new goog.events.Event "scene loaded")))))


