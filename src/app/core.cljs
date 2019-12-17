(ns app.core
  (:require [app.repl :as repl]
            [app.util.frame :as frame]
            [app.render :as renderer]
            [app.scenes :as scenes]
            [app.util.time :as time]
            [app.controllers :as controllers])
   (:import goog.events))


(def resizeArray (array))
(set! js/window.onresize
      (fn [e] (.forEach resizeArray
                        (fn [ el idx arr ] (.resize el)))))

(def sceneFile "scn/baseStation.gltf")


(defn ^:export main [ ]

  (def scenes (.getElementById js.document "scenes"))
  
  ; different scenes
  (def canvasContainer (.getElementById js.document "canvasContainer"))
  (def canvas (.getElementById js.document "canvas"))

  (def logo (.getElementById js.document "logo"))
  (def contact (.getElementById js.document "contact"))
  (def pitch (.getElementById js.document "pitch"))
  
  ; controls
  (def navControls(.getElementById js.document "navControls"))
  
  (def ncHome (.getElementById
               (. navControls -contentDocument) "home"))
  (def ncHomeBack (.getElementById
                   (. navControls -contentDocument) "homeBackground"))

  (def ncBaseStation (.getElementById
               (. navControls -contentDocument) "baseStation"))
  (def ncBaseStationBack (.getElementById
                   (. navControls -contentDocument) "baseStationBackground"))
  
  (def ncPitch (.getElementById
               (. navControls -contentDocument) "pitch"))
  (def ncPitchBack (.getElementById
                   (. navControls -contentDocument) "pitchBackground"))

  (def ncContact (.getElementById
               (. navControls -contentDocument) "contact"))
  (def ncContactBack (.getElementById
                   (. navControls -contentDocument) "contactBackground"))

  (def ncGithub (.getElementById
               (. navControls -contentDocument) "github"))
  (def ncGithubBack (.getElementById
                   (. navControls -contentDocument) "githubBackground"))
  
  ; scene controls

    (def sceneControls (.getElementById js.document "sceneControls"))
    (def resetSceneButton (.getElementById
               (. sceneControls -contentDocument) "reset"))


  ;app

  (def navControlsList (list ncHome ncContact ncBaseStation ncPitch ncGithub))

  (def dispatcher (new goog.events.EventTarget)) 

  (renderer/init canvas canvasContainer) 

  (def clock (.init (time/Clock.)))
  (def frameLoop (.init (new frame/FrameLoop clock)))
  (def pulser (.init (new time/Pulser 20 ))) ; 20fps pulser
  (.add clock pulser)

  (def spinner (.init (new controllers/Spinner
                             (.getElementById js.document "spinner")
                      (array
                       (.querySelector js.document ".ring div:nth-child(1)")
                       (.querySelector js.document ".ring div:nth-child(2)")
                       (.querySelector js.document ".ring div:nth-child(3)")))))

 (def renderControl (js-obj
      "run"  (fn [] (.render renderer/webgl scenes/scene scenes/camera))
      "active" false))

  
  (set! ncHome.controller
      (.init (new controllers/ControlButton
                  ncHome
                  logo
                  nil
                  nil
                  nil
                  nil )
             ncHomeBack
             navControlsList
             .2))

  (set! ncPitch.controller
      (.init (new controllers/ControlButton
                  ncPitch
                  pitch
                  nil
                  nil
                  nil
                  nil)
             ncPitchBack
             navControlsList
             .2))

  (set! ncContact.controller
      (.init (new controllers/ControlButton
                  ncContact
                  contact
                  nil
                  nil
                  nil
                  nil)
             ncContactBack
             navControlsList
             .2))

  (set! ncBaseStation.controller
        (.init (new controllers/ControlButton
                  ncBaseStation
                  canvasContainer  
                  (fn []    ;onActivate
                    (if scenes/loaded
                      (do
                        (set! canvas.style.display nil)
                        (set! (.-active renderControl) true))
                      (do
                        (.run spinner)
                        (scenes/init sceneFile dispatcher))))
                  nil;    ;onDeactivate
                  nil    ; onAfterUp
                  (fn []  ;onAfterDown
                    (set! renderControl.active false) 
                    (set! canvas.style.display "none"))) 
                    ncBaseStationBack
                    navControlsList
                    .2 ))
                    
    
  (set! ncGithub.controller
        (.init (new controllers/ControlButton
                    ncGithub
                    nil  ; target
                    (fn [] ; onActivate
                      (.activate ncHome.controller)
                      (.deactivate ncGithub.controller)
                      (.open js.window
                         "https://github.com/tecumsehcommunications/StarlinkViewer"
                         "_blank"))
                    nil ; onDeactivate
                    nil ; onAfterUp
                    nil ) ; onAfterDown
                    ncGithubBack
                    navControlsList
                    0))
  
 (.add pulser (. ncHome -controller))
 (.add pulser (. ncContact -controller))
 (.add pulser (. ncPitch -controller))
 (.add pulser (. ncBaseStation -controller))

 (.activate ncHome.controller) 


 (.push frameLoop.xArray renderControl)
 (.run frameLoop)
  
 (.listenOnce dispatcher "scene loaded"
              (fn [e]
                (set! canvasContainer.style.opacity 1)
                (set! canvas.style.display nil)
                (def controls (new js.THREE.OrbitControls scenes/camera canvas))
                (set! resetSceneButton.onclick
                      (fn [evt]
                        (.preventDefault evt)
                        (.reset controls)))

                (.stop spinner)
                (set! renderControl.active true)))
  
)
