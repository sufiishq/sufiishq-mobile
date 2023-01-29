package pk.sufiishq.app.di.qualifier

import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class HelpJson

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class OfflineResolver

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class OnlineResolver