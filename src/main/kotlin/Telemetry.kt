package org.example

import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor

fun buildOpenTelemetry(): OpenTelemetry {
    val exporter = OtlpGrpcSpanExporter.builder()
        .setEndpoint("http://localhost:4317") // e.g. local Jaeger or Grafana Agent
        .build()

    val tracerProvider = SdkTracerProvider.builder()
        .addSpanProcessor(BatchSpanProcessor.builder(exporter).build())
        .build()

    return OpenTelemetrySdk.builder()
        .setTracerProvider(tracerProvider)
        .buildAndRegisterGlobal()
}