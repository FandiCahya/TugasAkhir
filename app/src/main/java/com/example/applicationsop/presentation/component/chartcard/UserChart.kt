package com.example.applicationsop.presentation.component.chartcard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applicationsop.ui.theme.BiruMuda
import com.example.applicationsop.ui.theme.Purple40
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.biru
import com.example.applicationsop.ui.theme.ijo
import com.example.applicationsop.ui.theme.kuning

@Composable
fun PengajuanChartCard(
    pending: Int,
    accepted: Int,
    rejected: Int,
    pengembangan: Int,
    pengujian: Int,
    finished: Int
) {
    val chartData = listOf(
        ChartBar("Pending", pending, kuning),
        ChartBar("Accepted", accepted, ijo),
        ChartBar("Rejected", rejected, abang),
        ChartBar("Dev", pengembangan, biru),
        ChartBar("Testing", pengujian, BiruMuda),
        ChartBar("Finished", finished, Purple40)
    )

    val maxValue = chartData.maxOfOrNull { it.value }?.toFloat() ?: 1f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "📊 Statistik Pengajuan",
                fontSize = 16.sp,
                color = Color(0xFF555555)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom
            ) {
                chartData.forEach { bar ->
                    val barHeightRatio = if (maxValue == 0f) 0f else bar.value / maxValue
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.height(130.dp)
                    ) {
                        Text(
                            text = bar.value.toString(),
                            fontSize = 11.sp,
                            color = Color(0xFF666666),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Box(
                            modifier = Modifier
                                .width(16.dp)
                                .fillMaxHeight(barHeightRatio)
                                .background(bar.color, RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = bar.label,
                            fontSize = 10.sp,
                            color = Color(0xFF666666)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Legend
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                itemVerticalAlignment = Alignment.CenterVertically
            ) {
                chartData.forEach { bar ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(bar.color, shape = RoundedCornerShape(2.dp))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = bar.label,
                            fontSize = 10.sp,
                            color = Color(0xFF777777)
                        )
                    }
                }
            }
        }
    }
}

data class ChartBar(
    val label: String,
    val value: Int,
    val color: Color
)
