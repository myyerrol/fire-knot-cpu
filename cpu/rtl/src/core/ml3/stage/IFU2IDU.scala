package cpu.core.ml3

import chisel3._
import chisel3.util._

import cpu.base._

class IFU2IDU extends Module with ConfigInst {
    val io = IO(new Bundle {
        val iReadyFrIFU = Input(Bool())
        val iReadyFrIDU = Input(Bool())
        val iPC         = Input(UInt(ADDR_WIDTH.W))
        val iPCNext     = Input(UInt(ADDR_WIDTH.W))

        val oValidToIFU = Output(Bool())
        val oValidToIDU = Output(Bool())
        val oPC         = Output(UInt(ADDR_WIDTH.W))
        val oPCNext     = Output(UInt(ADDR_WIDTH.W))
        val oInst       = Output(UInt(INST_WIDTH.W))
    })

    io.oValidToIFU := true.B
    io.oValidToIDU := true.B

    val rPC     = RegEnable(io.iPC,     ADDR_INIT, io.oValidToIFU && io.iReadyFrIFU)
    val rPCNext = RegEnable(io.iPCNext, ADDR_INIT, io.oValidToIFU && io.iReadyFrIFU)

    io.oPC     := Mux(io.oValidToIDU && io.iReadyFrIDU, rPC,     ADDR_INIT)
    io.oPCNext := Mux(io.oValidToIDU && io.iReadyFrIDU, rPCNext, ADDR_INIT)
    io.oInst   := INST_ZERO
}
