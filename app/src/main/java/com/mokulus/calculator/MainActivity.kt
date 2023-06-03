package com.mokulus.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mokulus.calculator.parser.EConstant
import com.mokulus.calculator.parser.PiConstant
import com.mokulus.calculator.parser.functions.AcosFunction
import com.mokulus.calculator.parser.functions.AsinFunction
import com.mokulus.calculator.parser.functions.AtanFunction
import com.mokulus.calculator.parser.functions.CosFunction
import com.mokulus.calculator.parser.functions.ExpFunction
import com.mokulus.calculator.parser.functions.Log10Function
import com.mokulus.calculator.parser.functions.NaturalLogFunction
import com.mokulus.calculator.parser.functions.SinFunction
import com.mokulus.calculator.parser.functions.SquareRootFunction
import com.mokulus.calculator.parser.functions.TanFunction
import com.mokulus.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column {
                        val model = viewModel<CalculatorViewModel>()
                        TopField(Modifier.weight(1f), text = model.text)
                        Numpad(
                            Modifier.weight(4f),
                            text = model.text,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NumpadButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    colors: ButtonColors? = null,
    content: @Composable (() -> Unit)
) {
    Button(
        modifier = modifier.fillMaxSize(),
        onClick = onClick,
        colors = colors ?: ButtonDefaults.buttonColors(),
        shape = RectangleShape
    ) {
        content()
    }
}

@Composable
fun TopField(modifier: Modifier = Modifier, text: String) {
    var multiplier by remember { mutableFloatStateOf(1f) }
    Row(
        modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(android.graphics.Typeface.MONOSPACE),
            fontSize = 40.sp * multiplier,
            maxLines = 1,
            overflow = TextOverflow.Visible,
            onTextLayout = {
                if (it.hasVisualOverflow) {
                    multiplier *= 0.90f
                }
            })
    }
}

@Composable
fun BasicTab(
    text: String,
    fontSize: TextUnit,
    modifier: Modifier = Modifier
) {
    val model = viewModel<CalculatorViewModel>()
    Row(modifier = modifier) {
        Column(modifier = Modifier.weight(3f)) {
            for (row in listOf(7, 4, 1)) {
                Row(modifier = Modifier.weight(1f)) {
                    for (i in row..row + 2) {
                        NumpadButton(modifier = Modifier.weight(1f),
                            onClick = { model.pushKey(i) }) {
                            Text("$i", fontSize = fontSize)
                        }
                    }
                }
            }
            Row(modifier = Modifier.weight(1f)) {
                NumpadButton(modifier = Modifier.weight(1f), onClick = { model.pushDot() }) {
                    Text(".", fontSize = fontSize)
                }
                NumpadButton(modifier = Modifier.weight(1f), onClick = { model.pushKey(0) }) {
                    Text("0", fontSize = fontSize)
                }
                NumpadButton(modifier = Modifier.weight(1f), onClick = { model.calculate() }) {
                    Text("=", fontSize = fontSize)
                }
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            NumpadButton(modifier = Modifier.weight(1f), onClick = { model.pop() }) {
                Text("<", fontSize = fontSize)
            }
            val operators = listOf(
                OperatorType.Divide,
                OperatorType.Multiply,
                OperatorType.Plus,
                OperatorType.Minus)
            val onClick: (OperatorType) -> (() -> Unit) = { op ->
                {
                    model.pushOperator(op)
                }
            }
            for (op in operators) {
                NumpadButton(modifier = Modifier.weight(1f), onClick = onClick(op)) {
                    Text(op.getSymbol(), fontSize = fontSize)
                }
            }
        }
    }
}

@Composable
fun AdvancedTab(
    text: String,
    fontSize: TextUnit,
    modifier: Modifier = Modifier
) {
    val model = viewModel<CalculatorViewModel>()
    Row(modifier = modifier) {
        Column(modifier = Modifier.weight(1f)) {
            Row(modifier = Modifier.weight(1f)) {
                NumpadButton(modifier = Modifier.weight(1f),
                    colors = if (!model.inverse) null
                    else ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary, contentColor = MaterialTheme.colorScheme.primary),
                    onClick = {
                        model.toggleInverse()
                    }) {
                    Text("INV", fontSize = fontSize)
                }
                NumpadButton(modifier = Modifier.weight(1f),
                    onClick = { model.toggleUseRadians() }) {
                    Text(if (model.useRadians) "RAD" else "DEG", fontSize = fontSize)
                }
                NumpadButton(modifier = Modifier.weight(1f), onClick = {
                    model.pushOperator(OperatorType.Percent)
                }) {
                    Text(OperatorType.Percent.getSymbol(), fontSize = fontSize)
                }
            }
            Row(modifier = Modifier.weight(1f)) {
                val trigFunctions =
                    if (!model.inverse)
                        listOf(
                            SinFunction(model.useRadians),
                            CosFunction(model.useRadians),
                            TanFunction(model.useRadians)
                        )
                    else
                        listOf(
                            AsinFunction(model.useRadians),
                            AcosFunction(model.useRadians),
                            AtanFunction(model.useRadians)
                        )
                for (trigFunc in trigFunctions) {
                    NumpadButton(modifier = Modifier.weight(1f),
                        onClick = {
                            model.pushFunction(trigFunc)
                        }) {
                        Text(trigFunc.name, fontSize = fontSize)
                    }
                }
            }
            Row(modifier = Modifier.weight(1f)) {
                val function = if (!model.inverse) ExpFunction() else NaturalLogFunction()
                NumpadButton(modifier = Modifier.weight(1f),
                    onClick = {
                        model.pushFunction(function)
                    }) {
                    Text(function.name, fontSize = fontSize)
                }
                NumpadButton(modifier = Modifier.weight(1f),
                    onClick = {
                        if (!model.inverse) {
                            model.pushKey(1)
                            model.pushKey(0)
                            model.pushOperator(OperatorType.Power)
                        } else {
                            model.pushFunction(Log10Function())
                            model.pushLeftParen()
                        }
                    }) {
                    Text(if (!model.inverse) "10${OperatorType.Power.getSymbol()}x" else Log10Function().name, fontSize = fontSize)
                }
                NumpadButton(modifier = Modifier.weight(1f),
                    onClick = { model.pushOperator(OperatorType.Factorial) }) {
                    Text("!", fontSize = fontSize)
                }
            }
            Row(modifier = Modifier.weight(1f)) {
                val piConstant = PiConstant()
                val eConstant = EConstant()
                NumpadButton(modifier = Modifier.weight(1f),
                    onClick = { model.pushConstant(piConstant) }) {
                    Text(piConstant.text, fontSize = fontSize)
                }
                NumpadButton(modifier = Modifier.weight(1f),
                    onClick = { model.pushConstant(eConstant) }) {
                    Text(eConstant.text, fontSize = fontSize)
                }
                NumpadButton(modifier = Modifier.weight(1f), onClick = {
                    model.pushOperator(OperatorType.Power)
                }) {
                    Text(OperatorType.Power.getSymbol(), fontSize = fontSize)
                }
            }
            Row(modifier = Modifier.weight(1f)) {
                NumpadButton(modifier = Modifier.weight(1f), onClick = {
                    model.pushLeftParen()
                }) {
                    Text("(", fontSize = fontSize)
                }
                NumpadButton(modifier = Modifier.weight(1f), onClick = {
                    model.pushRightParen()
                }) {
                    Text(")", fontSize = fontSize)
                }
                NumpadButton(modifier = Modifier.weight(1f),
                    onClick = {
                        if (!model.inverse) {
                            model.pushOperator(OperatorType.Power)
                            model.pushKey(2)
                        } else {
                            model.pushFunction(SquareRootFunction())
                        }
                    }) {
                    Text(if (!model.inverse) "x${OperatorType.Power.getSymbol()}2" else SquareRootFunction().name, fontSize = fontSize)
                }
            }
        }
    }
}

@Composable
fun Numpad(
    modifier: Modifier = Modifier,
    text: String
) {
    var state by remember { mutableIntStateOf(0) }
    val fontSize = 30.sp
    val titles = listOf("Basic", "Advanced")
    val inAnimation = tween<IntOffset>(200, easing = CubicBezierEasing(0.65f, 0f, 0.35f, 1f))
    Column(modifier = modifier) {
        AnimatedContent(modifier = Modifier.weight(8f), targetState = state, transitionSpec = {
            if (targetState > initialState) {
                slideInHorizontally(animationSpec = inAnimation) { width -> width } togetherWith slideOutHorizontally(
                    animationSpec = inAnimation
                ) { width -> -width }
            } else {
                slideInHorizontally(animationSpec = inAnimation) { width -> -width } togetherWith slideOutHorizontally(
                    animationSpec = inAnimation
                ) { width -> width }
            }
        }) { targetState ->
            when (targetState) {
                0 -> BasicTab(text, fontSize)
                1 -> AdvancedTab(text, fontSize)
            }
        }
        TabRow(state, modifier = Modifier.weight(1f)) {
            titles.forEachIndexed { index, title ->
                Tab(selected = state == index, onClick = { state = index }, text = { Text(title) })
            }
        }
    }
}