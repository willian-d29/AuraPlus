package com.d29.practica5

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Preview(showBackground = true)
@Composable
fun Layo() {
    ConstraintLayout(modifier = Modifier.fillMaxSize().background(Color.DarkGray)) {
        // Define las referencias de los boxes
        val (cajaRed, cajaGreen, cajaYellow, cajaBlue, cajaMagenta, cajaCyan, whiteBox1, whiteBox2, whiteBox3) = createRefs()

        // Box verde
        Box(
            modifier = Modifier
                .size(126.dp)
                .background(Color.Green)
                .constrainAs(cajaGreen) {
                    end.linkTo(cajaMagenta.start)
                    bottom.linkTo(cajaMagenta.top)
                }
        )

        // Box amarillo
        Box(
            modifier = Modifier
                .size(126.dp)
                .background(Color.Yellow)
                .constrainAs(cajaYellow) {
                    start.linkTo(cajaMagenta.end)
                    bottom.linkTo(cajaMagenta.top)
                }
        )

        // Box rojo
        Box(
            modifier = Modifier
                .size(126.dp)
                .background(Color.Red)
                .constrainAs(cajaRed) {
                    start.linkTo(cajaMagenta.end)
                    top.linkTo(cajaMagenta.bottom)
                }
        )

        // Box azul
        Box(
            modifier = Modifier
                .size(126.dp)
                .background(Color.Blue)
                .constrainAs(cajaBlue) {
                    end.linkTo(cajaMagenta.start)
                    top.linkTo(cajaMagenta.bottom)
                }
        )

        // Box cian
        Box(
            modifier = Modifier
                .size(126.dp)
                .background(Color.)
                .constrainAs(cajaCyan) {
                    start.linkTo(cajaMagenta.end)
                    top.linkTo(cajaMagenta.bottom)
                }
        )

        // Box magenta (centro)
        Box(
            modifier = Modifier
                .size(126.dp)
                .background(Color.Magenta)
                .constrainAs(cajaMagenta) {
                    centerVerticallyTo(parent)
                    centerHorizontallyTo(parent)
                }
        )

        // White boxes (staircase)
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(Color.Cyan)
                .constrainAs(whiteBox1) {
                    bottom.linkTo(cajaMagenta.top)
                    start.linkTo(cajaGreen.end)
                }
        )
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(Color.White)
                .constrainAs(whiteBox2) {
                    bottom.linkTo(whiteBox1.top)
                    start.linkTo(whiteBox1.end)
                }
        )
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(Color.White)
                .constrainAs(whiteBox3) {
                    bottom.linkTo(whiteBox2.top)
                    start.linkTo(whiteBox2.end)
                }
        )
    }
}