package com.example.benne.daisyapp2.data.daisy202

import org.jsoup.nodes.Element

/**
 * Created by benne on 6/01/2018.
 */
data class SmilTextElement(
    val id: String,
    val file: String,
    val Fragment: String
) {
    constructor(elem: Element)
        : this(
            elem.attr("id"),
            elem.attr("src").split("#")[0],
            elem.attr("src").split("#")[0]
        )
}

data class SmilParElement(
        val id: String,
        val text: SmilTextElement,
        val audio: SmilAudioElement?,
        val nestedSeq: List<SmilNestedSeqElement>
)

data class SmilNestedSeqElement(
        val audioReferences: List<SmilAudioElement>
) {

}

data class SmilAudioElement(
        val id: String,
        val file: String,
        val clipStart: Long?,
        val clipEnd: Long?
) {
    constructor(elem: Element)
            : this(
            elem.attr("id"),
            elem.attr("src"),
            elem.attr("clip-begin")
                .replace("npt=","")
                .replace("s","")
                .toMaybeMicroSeconds(),
            elem.attr("clip-end")
                .replace("npt=","")
                .replace("s","")
                .toMaybeMicroSeconds())

}