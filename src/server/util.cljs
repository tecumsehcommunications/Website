(ns server.util
  (:require [cljs.nodejs :as node])
  (:import goog.dom))

  (def root "/usr/local/lib/node_modules/")

;  (def fs (node/require "fs"))
;  (def gm (node/require (str root "gm")))
;  (def parser (node/require (str root "node-html-parser")))
(def mongodb (node/require (str root "mongodb")))
(def wss (node/require (str root "ws")))

(def uidgen (node/require (str root "uuid")))
(def xhr (node/require (str root "request")))

(defn couchGet [ msg ]
  (.get xhr (str "http://admin:Pa$$w0rd@127.0.0.1:5984/" msg)
        (fn [err res body]
          (.log js/console body))))


(defn couchPut [ msg ]
  (.put xhr (str "http://admin:Pa$$w0rd@127.0.0.1:5984/" msg)
        (fn [err res body]
          (.log js/console body))))

(defn createDoc [ database docObj ]
  (.put xhr (js-obj
                "url" (str "http://admin:Pa$$w0rd@35.212.206.166:5984/"
                           database
                           "/"
                           (.replace clojure.string (.v4 uidgen) "-" ""))
                "body"  (.stringify js/JSON docObj))))

(defn getCards []
  (.get xhr "http://127.0.0.1:5984/flashcards/_design/dDoc1/_view/v1"
        (fn [ err res body ] (.log js/console body))))
(getCards)

 (do
(def c1 (js-obj "seqno" 1 "english" "person" "pinyin" "rén" "chinese" "人"))
(def c2 (js-obj "seqno" 2 "english" "knife" "pinyin" "dāo" "chinese" "刀"))
(def c3 (js-obj "seqno" 3 "english" "power" "pinyin" "lì" "chinese" "力"))
(def c4 (js-obj "seqno" 4 "english" "right hand; again" "pinyin" "yòu" "chinese" "又"))
(def c5 (js-obj "seqno" 5 "english" "mouth" "pinyin" "kǒu" "chinese" "口"))
(def c6 (js-obj "seqno" 6 "english" "enclose" "pinyin" "wéi" "chinese" "囗"))
(def c7 (js-obj "seqno" 7 "english" "earth" "pinyin" "tǔ" "chinese" "土"))
(def c8 (js-obj "seqno" 8 "english" "sunset" "pinyin" "xī" "chinese" "夕"))
(def c9 (js-obj "seqno" 9 "english" "big" "pinyin" "dà" "chinese" "大"))
(def c10 (js-obj "seqno" 10 "english" "woman" "pinyin" "nǚ" "chinese" "女"))
(def c11 (js-obj "seqno" 11 "english" "son" "pinyin" "zǐ" "chinese" "子"))
(def c12 (js-obj "seqno" 12 "english" "inch" "pinyin" "cùn" "chinese" "寸"))
(def c13 (js-obj "seqno" 13 "english" "small" "pinyin" "xiǎo" "chinese" "小"))
(def c14 (js-obj "seqno" 14 "english" "labor; work" "gōng" "tǔ" "chinese" "工"))
(def c15 (js-obj "seqno" 15 "english" "tiny; small" "pinyin" "yāo" "chinese" "幺"))
(def c16 (js-obj "seqno" 16 "english" "bow" "pinyin" "gōng" "chinese" "弓"))
(def c17 (js-obj "seqno" 17 "english" "heart" "pinyin" "xīn" "chinese" "心"))
(def c18 (js-obj "seqno" 18 "english" "dagger-axe" "pinyin" "gē" "chinese" "戈"))
(def c19 (js-obj "seqno" 19 "english" "hand" "pinyin" "shǒu" "chinese" "手"))
(def c20 (js-obj "seqno" 20 "english" "sun" "pinyin" "rì" "chinese" "日"))
(def c21 (js-obj "seqno" 21 "english" "moon" "pinyin" "yuè" "chinese" "月"))
(def c22 (js-obj "seqno" 22 "english" "wood" "pinyin" "mù" "chinese" "木"))
(def c23 (js-obj "seqno" 23 "english" "water" "pinyin" "shuǐ" "chinese" "水"))
(def c24 (js-obj "seqno" 24 "english" "fire" "pinyin" "huǒ" "chinese" "火"))
(def c25 (js-obj "seqno" 25 "english" "field" "pinyin" "tián" "chinese" "田"))
(def c26 (js-obj "seqno" 26 "english" "eye" "pinyin" "mù" "chinese" "目"))
(def c27 (js-obj "seqno" 27 "english" "show" "pinyin" "shì" "chinese" "示"))
(def c28 (js-obj "seqno" 28 "english" "fine silk" "pinyin" "mì" "chinese" "纟"))
)

(createDoc "cards" c28)


(couchGet "_all_dbs")


  (def livereload (node/require (str root "livereload")))

  (defn livereload-on [ directory ]
    (let [ lrServer (.createServer livereload
                    (js-obj "exts"
                            (array "html" "svg" "css" "png" "jpg" "gif")))
          fileWatcher (.watch lrServer directory) ]))


 (def lrHandle (livereload-on "/var/www/html/"))


;;;;

(def mongoClient (. mongodb -MongoClient))
(def mongoURL "mongodb://localhost:27017")
(def dbClient (new mongoClient mongoURL))
(.then  (.connect dbClient) (.log js/console "successful db connection"))
(def db (.db dbClient "flashcards"))(d
(def collection (.collection db "documents"))
(.insertOne collection (def mike #js {:_id "4EBA" :pinyin "ren" :english "person" }  )
(def mike (.find collection #js {:_id "4EBA"})) 
(.each mike (fn [ err item] (.log js/console item)))
            
(.close db)
(js-obj 

  (defn newWss [ port ]
      (new wss.Server (js-obj "port" port)))  


  (deftype noradImageFetcher [ tempDir archive ] Object
           (init [ this time ]
             (set! this.time (str time))
             (set! this.wrkTime (str nil))
             (set! this.files "none")
             (set! this.lastFile "none")
             (set! this.timerHandle nil)
             (set! this.baseUrl  "https://radar.weather.gov/RadarImg/N0R/BHX/")                          
             (set! this.reqObject (js-obj
                                       "headers" (js-obj
                                                  "Host" "radar.weather.gov"
                                                  "User-Agent" "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:66.0) Gecko/20100101 Firefox/66.0"   
                                                  "Accept" "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"
                                                  "Accept-Language" "en-US,en;q=0.5"
                                                  "Connection" "keep-alive"
                                                  "Upgrade-Insecure-Requests" 1)))
             (set! this.sleepTime  300000) ; 5 mins in ms
             (set! this.stdErr (fn [err] (if err (js.console.log err))))
           this)

           (queue [this]
             (set! this.timerHandle (js.setTimeout (fn [] (.getPic this)) this.sleepTime)))

           (dequeue [this]
             (if this.timerHandle
               (do
                 (js.clearTimeout this.timerHandle)
                 (set! this.timerHandle nil))))
        
           (scrubDir [ this ]
             (.readdir fs this.archive
                       (fn [err files] ; files is a js array
                         (let [ numFiles (- (.-length files) 1)
                                excess (- numFiles 61) ] ; no more than 61 pic in directory
                           (if (> excess 0)
                             (do
                               (set! this.files (.slice files (- excess 1) files.length))
                               (doseq [x (range excess) ]
                                 (.unlink fs
                                          (str this.archive (aget files x)) this.stdErr)))
                             (set! this.files files))
                           (set! this.lastFile
                               (aget files
                                     (- files.length 1)))))))
                           
           (getPic [this]
                (request.get
                    this.baseUrl  
                    this.reqObject
                   (fn [err res body]
                       (if err
                          (do
                             (js.console.log "norad page request error")
                             (set! this.sleepTime 60000) ; wait a minute and try again)
                             (.queue this))
                          (let [ tableRows (.reverse ; reversing table to start at bottom 
                                             (goog.dom.getChildren
                                              (.querySelector (parser.parse body) "table"))) 
                                 goodTable (.slice tableRows 0 (- (. tableRows -length) 3 )) ]
                                                        
                            (loop [ cnt 1 ]
                              (if (and (< cnt 20 ) (aget goodTable cnt))
                                (let [ name (.-innerHTML (goog.dom.getFirstElementChild
                                                            (aget (goog.dom.getChildren
                                                                (aget goodTable cnt)) 1)))
                                       year  (.substring name 4 8)
                                       month (.substring name 8 10)
                                       day   (.substring name 10 12)
                                       time  (.substring name 13 17)
                                       url   (str this.baseUrl name)
                                       tempfile (str this.tempDir "norad-" cnt ".gif")
                                       fname (str this.archive year month day time ".png")  ]
                                  
                                  (if (= 1 cnt) (set! this.wrkTime time))
                                  (if (= this.wrkTime this.time)
                                    (set! this.sleepTime 300000)
                                    (do
                                        (.pipe
                                          (request.get
                                            (str this.baseUrl name)
                                            this.reqObject)
                                          (.on
                                            (fs.createWriteStream tempfile)
                                            "finish"
                                            (fn []
                                              (js.console.log "tempfile name: " tempfile)
                                              (.write
                                               (.resize 
                                                (.rotate
;                                                (.resize
                                                  (gm tempfile)
;                                                  1600 1100)
                                                  "transparent" -12.5)
                                                1024 1024 "!")
                                               fname
                                               (fn [err]
                                                 (if err
                                                   (do   (js.console.log "norad image write error")
                                                           (js.console.log err)))
                                                 (.unlink fs tempfile this.stdErr))))))
                                        (recur (inc cnt)))))))
                          (set! this.sleepTime 300000)  
                          (set! this.time this.wrkTime)
                          (.scrubDir this)
                          (.queue this )))))))                                        

  (deftype geosImageFetcher [ tempDir archive ] Object
    ; require node filesystem (as fs) and node request as rs in scope       
           (init  [this time ]
             (set! this.time (str time))
             (set! this.files "none")
             (set! this.lastFile "none")
             (set! this.timerHandle nil)
             (set! this.reqString  "https://weather.msfc.nasa.gov/cgi-bin/get-abi?satellite=GOESWestconusband02&lat=40.4&lon=-124.0&zoom=1&width=558&height=992&quality=100&mapcolor=yellow")
             (set! this.urlPrefix  "https://weather.msfc.nasa.gov")
             (set! this.tempFile   (str tempDir "nasageo.jpg"))
             (set! this.sleepTime  300000) ; 5 mins in ms
             (set! this.stdErr (fn [err] (if err (js.console.log err))))          
           this)

           (queue [this]
             (set! this.timerHandle (js.setTimeout (fn [] (.getPic this)) this.sleepTime)))

           (dequeue [this]
             (if this.timerHandle
               (do
                 (js.clearTimeout this.timerHandle)
                 (set! this.timerHandle nil))))

           (scrubDir [ this ]
             (.readdir fs this.archive
                       (fn [err files] ; files is a js array
                         (let [ numFiles (- (.-length files) 1)
                                excess (- numFiles 61) ] ; no more than 61 pics in directory
                           (if (> excess 0)
                             (do
                               (set! this.files (.slice files (- excess 1) files.length))
                               (doseq [x (range excess) ]
                                 (.unlink fs (str this.archive (aget files x)) this.stdErr)))
                             (set! this.files files))
                           (set! this.lastFile
                               (aget files
                                    (- files.length 1)))))))           
           (getPic [this]
             (request.get
               this.reqString
               (fn [err res body]
                 (if err
                   (do
                     (js.console.log "geos satellite picture request error")
                     (set! this.sleepTime 60000)
                     (.queue this))
                   (let [ start   (+ 1 (.indexOf body "\"" (.indexOf body  "<IMG")))
                          end     (.indexOf body "\"" start)
                          srcStr  (.substring body start end)
                          fnStart (+ (.indexOf srcStr "/GOES") 5)
                          time (.substring srcStr fnStart (+ fnStart 4))
                          year (.substring srcStr (+ fnStart 4) (+ 8 fnStart))
                          day (.substring srcStr (+ fnStart 8) (+ 11 fnStart))
                          url (str "https://weather.msfc.nasa.gov" srcStr )                         
                          fname (str this.archive year day time ".jpg")  ]
           
                     (if (= time this.time)
                       (do
                         (set! this.sleepTime 60000) ; try again in one minute
                         (.queue this))
                       (do
                         (set! this.sleepTime 300000) ; five minute wait                          
                         (set! this.time time)
                         (.pipe
                          (request url)
                          (.on
                           (fs.createWriteStream this.tempFile)
                           "finish"
                           (fn []
                             (.write
                              (.resize
                                 (.crop
                                      (gm this.tempFile)
                                        558
                                        932
                                        0
                                        60)
                                 512 512 "!")
                                 fname
                                 (fn [err]
                                   (if err
                                     (do
                                       (js.console.log "nasa image write error")
                                       (js.console.log err))
                                     (do
                                       (.scrubDir this)
                                       (.queue this))))))))))))))))

