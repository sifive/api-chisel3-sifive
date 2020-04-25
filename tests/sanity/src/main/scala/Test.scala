
import chisel3._

class Test extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(8.W))
    val out = Output(UInt(8.W))
  })
  io.out := io.in + 1.U
}

object TestMain extends App {
  chisel3.Driver.execute(args, () => new Test)
}
