package com.naonworks.common.config

import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.web.reactive.config.ResourceHandlerRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer

// 성능 최적화 및 불필요한 프록시 생성을 피하기 위해서 사용,
// 이 설정을 사용하면 구성 클래스에서 반환하는 객체는 항상 새로운 인스턴스가 되며, 다른 빈에서 구성 클래스의 빈을 호출할 때 프록시 객체가 생성되지 않아 성능을 높일 수 있습니다.
@Configuration(proxyBeanMethods = false)
// WebFluxConfigurer는 Spring WebFlux 프레임워크에서 웹 애플리케이션의 구성을 설정하기 위한 인터페이스이며 웹 애플리케이션의 다양한 측면을 커스터마이징하고 조정할 수 있는 메소드를 제공
class WebFluxConfig : WebFluxConfigurer {

    // 정적 자원의 위치를 지정하는 용도로 사용되는 배열을 정의
    private val CLASSPATH_RESOURCE_LOCATIONS = arrayOf(
        "classpath:/META-INF/resources/",
        "classpath:/resources/", "classpath:/static/", "classpath:/public/"
    )

    // HTTP 요청 및 응답의 코덱을 조정하고 구성, 메모리에 저장되는 최대 크기를 무제한(-1)으로 설정, 대용량 파일 업로드와 같은 작업을 수행할 때 좀 더 편리하게 처리 가능. 하지만 메모리 소비에 주의
    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        configurer.defaultCodecs().maxInMemorySize(-1)
    }

    // 정적 자원의 핸들링 및 리졸빙과 관련된 설정,
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
            .setOrder(0) // 리소스 핸들러의 우선 순위(Order)를 0으로 설정
            .addResourceHandler("", "/**") // 요청 경로 패턴을 설정. 여기서는 모든 경로에 대해서 정적 자원 핸들러를 등록
            .addResourceLocations(* CLASSPATH_RESOURCE_LOCATIONS) // 정적 자원이 위치할 디렉터리를 설정. 앞서 정의한 CLASSPATH_RESOURCE_LOCATIONS 배열에 따라 클래스패스 상의 여러 디렉터리를 설정
            .resourceChain(true) // 리소스 체인을 활성화합니다. 리소스 체인은 캐싱, 버전 관리 등을 지원하여 정적 자원을 처리할 때 유용합니다.
            .addResolver(CustomEncodedResourceResolver(ClassPathResource("/static/index.html"))) // 커스텀한 리소스 리졸버를 추가합니다. 여기서는 CustomEncodedResourceResolver 클래스를 사용하여 /static/index.html 경로에 대한 인코딩된 리소스를 처리하도록 설정
    }
}