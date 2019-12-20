package csw.framework

import csw.command.client.models.framework.{ComponentInfo, LocationServiceUsage}
import csw.framework.internal.wiring.{CswFrameworkSystem, FrameworkWiring}
import csw.framework.models.CswContext
import csw.location.models.ComponentType
import csw.prefix.models.Prefix
import utils.Extensions.FutureExt

import scala.concurrent.duration.{DurationInt, FiniteDuration}

class CswClientWiring {
  lazy val wiring = new FrameworkWiring
  import wiring._
  import actorRuntime._

  lazy val timeout: FiniteDuration = 20.seconds
  implicit lazy val cswFrameworkSystem: CswFrameworkSystem =
    new CswFrameworkSystem(typedSystem)

  lazy val cswContext: CswContext =
    CswContext
      .make(
        locationService,
        eventServiceFactory,
        alarmServiceFactory,
        // dummy component info, it is not used by csw-client
        ComponentInfo(
          Prefix("csw.client"),
          ComponentType.Service,
          "",
          LocationServiceUsage.DoNotRegister
        )
      )
      .await()
}
