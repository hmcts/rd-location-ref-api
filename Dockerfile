ARG APP_INSIGHTS_AGENT_VERSION=3.4.9
ARG PLATFORM=""
# Application image

FROM hmctspublic.azurecr.io/base/java${PLATFORM}:17-distroless

COPY lib/applicationinsights.json /opt/app/
COPY build/libs/rd-location-ref-api.jar /opt/app/

EXPOSE 8099
CMD [ "rd-location-ref-api.jar" ]
