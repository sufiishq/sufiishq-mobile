package pk.sufiishq.aurora.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS, AnnotationTarget.ANNOTATION_CLASS)
internal annotation class ExcludeFromJacocoGeneratedReport
