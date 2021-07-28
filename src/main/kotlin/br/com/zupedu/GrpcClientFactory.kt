package br.com.zupedu

import br.com.zup.edu.KeyManagerBuscaGrpcServiceGrpc
import br.com.zup.edu.KeyManagerListaGrpcServiceGrpc
import br.com.zup.edu.KeyManagerRegistraGrpcServiceGrpc
import br.com.zup.edu.KeyManagerRemoveGrpcServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory(@GrpcChannel("keyManagerGrpc") val channel: ManagedChannel) {

    @Singleton
    fun keyManageRegistraGrpcStub() = KeyManagerRegistraGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun keyManagerRemoveGrpcStub() = KeyManagerRemoveGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun keyManagerDetalheGrpcStub() = KeyManagerBuscaGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun keyManagerListarGrpcStub() = KeyManagerListaGrpcServiceGrpc.newBlockingStub(channel)
}