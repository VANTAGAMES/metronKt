package metron.util

import com.github.quillraven.fleks.*
import korlibs.datastructure.iterators.*

abstract class ComponentHooks<T> : ComponentType<T>() {
    open val onAdded: ComponentHook<T> = { _, _ -> }
    open val onRemoved: ComponentHook<T> = { _, _ -> }
}

inline fun <reified T : Component<*>> ComponentConfiguration.add(vararg componentHooksList: ComponentHooks<T>) {
    componentHooksList.fastForEach { componentHooks ->
        onAdd(componentHooks, componentHooks.onAdded)
        onRemove(componentHooks, componentHooks.onRemoved)
    }
}
