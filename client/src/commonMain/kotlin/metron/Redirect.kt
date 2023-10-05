package metron

typealias Redirector = suspend (String) -> Unit

lateinit var redirector: Redirector


