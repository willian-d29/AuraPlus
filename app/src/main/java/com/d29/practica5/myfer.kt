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
fun ConstraintExample() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (cajaGreen, cajaYellow, cajaRed, cajaCyan, cajaMagenta) = createRefs()
        val (cajaBlue, cajaBlack) = createRefs()

        Box(
            modifier = Modifier
                .size(125.dp)
                .background(Color.Blue)
                .constrainAs(cajaBlue) {
                    end.linkTo(cajaRed.start)
                    bottom.linkTo(cajaRed.top)
                }
        )

        Box(
            modifier = Modifier
                .size(125.dp)
                .background(Color.Black)
                .constrainAs(cajaBlack) {
                    end.linkTo(cajaCyan.start)
                    top.linkTo(cajaCyan.bottom)
                }
        )

        Box(
            modifier = Modifier
                .size(125.dp)
                .background(Color.Green)
                .constrainAs(cajaGreen) {
                    end.linkTo(cajaMagenta.start)
                    top.linkTo(cajaMagenta.bottom)
                }
        )

        Box(
            modifier = Modifier
                .size(125.dp)
                .background(Color.Yellow)
                .constrainAs(cajaYellow) {
                    end.linkTo(cajaMagenta.start)
                    bottom.linkTo(cajaMagenta.top)
                }
        )

        Box(
            modifier = Modifier
                .size(125.dp)
                .background(Color.Red)
                .constrainAs(cajaRed) {
                    start.linkTo(cajaMagenta.end)
                    bottom.linkTo(cajaMagenta.top)
                }
        )

        Box(
            modifier = Modifier
                .size(125.dp)
                .background(Color.Cyan)
                .constrainAs(cajaCyan) {
                    start.linkTo(cajaMagenta.end)
                    top.linkTo(cajaMagenta.bottom)
                }
        )

        Box(
            modifier = Modifier
                .size(125.dp)
                .background(Color.Magenta)
                .constrainAs(cajaMagenta) {
                    centerVerticallyTo(parent)
                    centerHorizontallyTo(parent)
                }
        )
    }
}

