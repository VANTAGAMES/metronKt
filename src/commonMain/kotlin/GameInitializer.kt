import effect.*

fun State.initScene() {
    isPaused = false
    initGame()
    ghostSpawner()
    score()
    combo()
    verdict()
    autoMacro()
    //                container.addUpdater((bpmToSec).timesPerSecond) {
//                    audit(0.seconds, null)
//                }
    magnanimityEffect {
        magnanimity = level.magnanimity
    }
    note.stickAngle.resetElapsed()
}

fun State.initGame() {
    progressbar()
    countdownText()
    musicPlayer()
}
