package com.martin.bibleapp.ui.document

object DocumentHtml {
    val STYLE = """
        <style>
            body { font-size: 16pt; line-height: 1.8; margin: 0px 20px 0px 20px }
            p { padding:0px; margin:0px; text-indent: 5% }
            span.verse-no { vertical-align: super; font-size: 0.6em; font-weight: 300 }
        </style>
        """.trimIndent()

    val JAVA_SCRIPT = """
        <script language="javascript">
        window.onscroll = function() { onScrollHandler() };
        
        function onScrollHandler() {
            let currentVerse = getCurrentVerse(Math.floor( document.body.scrollTop ));;
            
            window.kmpJsBridge.callNative(
                "currentVerse",
                currentVerse,
                null
            );
        }

        function getCurrentVerse(offset) {
            console.log("get Current verse below: " + offset);
            return getVerseOffsets().find(function (el) {
                return el.offset > offset;
            })?.osisId;           
        }
        
        var verseOffsets = null;
        function getVerseOffsets() {
            if (verseOffsets) {
                return verseOffsets;
            }
            
            console.log("Calculate verse offsets");

            var verseNodeList = document.querySelectorAll('span.verse-no');
            let tempOffsets = [];
            for(let i=0; i < verseNodeList.length; i++) {
              let node = verseNodeList[i];
              const record = {
                 osisId: node.id,
                 offset: Math.floor(node.getBoundingClientRect().top + window.scrollY)
              }
              tempOffsets.push(record);
            }
            verseOffsets = tempOffsets;
            return verseOffsets
        }

        </script>          
""".trimIndent()
}