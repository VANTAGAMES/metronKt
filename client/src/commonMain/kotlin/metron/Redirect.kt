package metron

typealias Redirector = suspend (String) -> Unit

lateinit var redirector: Redirector


var LoginTokenSetter: ((String) -> Unit)? = null
var LoginTokenGetter: (() -> String)? = null

var clientWebsite: (() -> String)? = null
