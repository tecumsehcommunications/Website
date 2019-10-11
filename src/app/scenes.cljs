(ns app.scenes
     (:import goog.events))

(def loaded false)
(def rotateControls(.getElementById js.document "rotateControls"))
  
(defn init [ sceneFile dispatcher ]
  
  (def rotateUp (.getElementById
               (. rotateControls -contentDocument) "rotateUp"))
  (def rotateDown (.getElementById
               (. rotateControls -contentDocument) "rotateDown"))
  (def rotateLeft (.getElementById
               (. rotateControls -contentDocument) "rotateLeft"))
  (def rotateRight (.getElementById
               (. rotateControls -contentDocument) "rotateRight"))

  (def reset (.getElementById
               (. rotateControls -contentDocument) "reset"))

  (def group (new js.THREE.Group))
  ; (def light (new js.THREE.AmbientLight 0xffffff))
  (def loader (new js.THREE.GLTFLoader))
  
  (.load loader sceneFile 
     (fn [ gltf ]
       (def scene gltf.scene)
       (def camera (aget gltf.cameras 0))
       
    ; setup if camera os ortho projection   
    ;   (let [ vY (/ js.window.innerHeight 2)
    ;          vX (/ js.window.innerWidth 2) ]
    ;     (set! camera.left (- vX))
    ;     (set! camera.right vX)
    ;     (set! camera.top vY)
    ;     (set! camera.bottom (- vY))
    ;     (.updateProjectionMatrix camera))

       (aset camera "aspect"
               (/ js.window.innerWidth js.window.innerHeight))
       (set! scene.aspect camera.aspect)
       (set! camera.fov 30)
       (.updateProjectionMatrix camera)
         ; tweak for camera coming out of blender which instatiates with an appx 17 fov
       
       (set! scene.resize (fn []
            (aset camera "aspect"
               (/ js.window.innerWidth js.window.innerHeight))
            (if (> (. camera -aspect) (. scene -aspect))
                (set! (. camera -zoom) (. scene -aspect))
                (set! (. camera -zoom) (. camera -aspect)))
            (.updateProjectionMatrix camera)))

     ; resize function if camera ia ortho projection
     ;  (set! scene.resize
     ;        (fn []
     ;          (let [ vY (/ js.window.innerHeight 2)
     ;                 vX (/ js.window.innerWidth 2) ]
     ;            (set! camera.left (- vX))
     ;            (set! camera.right vX)
     ;            (set! camera.top vY)
     ;            (set! camera.bottom (- vY)) 
     ;            (.updateProjectionMatrix camera))))
       
       (.push app.core.resizeArray scene)
  
       (let [ m1 (aget scene "children" 2)
              m2 (aget scene "children" 3)
              m3 (aget scene "children" 4) ]
         
         (.add group m1)
         (.add group m2)
         (.add group m3)
         (.add scene group))

       (set! rotateLeft.onclick (fn [evt]
                        (.preventDefault evt)
                        (.rotateZ group  (* 30 (/ js.Math.PI 180)))))

       (set! rotateRight.onclick (fn [evt]
                        (.preventDefault evt)
                        (.rotateZ group  (* -30 (/ js.Math.PI 180)))))

       (set! rotateUp.onclick (fn [evt]
                        (.preventDefault evt)
                        (.rotateX group  (* 30 (/ js.Math.PI 180)))))

       (set! rotateDown.onclick (fn [evt]
                        (.preventDefault evt)
                                  (.rotateX group  (* -30 (/ js.Math.PI 180)))))

       (set! reset.onclick (fn [evt]
                             (.preventDefault evt)
                             (.set group.rotation 0 0 0)))

;       (.add scene light)
       (set! scene.background
             (new js.THREE.Color 0xfefbd8))
       (set! loaded true)
       (.dispatchEvent dispatcher
             (new goog.events.Event "scene loaded")))))
