package io.github.yuko1101.asterium.events

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class EventTrigger(val priority: EventPriority = EventPriority.THIRD)