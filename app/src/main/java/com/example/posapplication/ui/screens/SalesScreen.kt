package com.example.posapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.posapplication.data.local.InventoryEntity
import com.example.posapplication.data.local.SalesEntity
import com.example.posapplication.viewmodel.POSViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesScreen(
    viewModel: POSViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var selectedItem by remember { mutableStateOf<InventoryEntity?>(null) }
    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Total Revenue", style = MaterialTheme.typography.labelMedium)
                Text(
                    text = "$${state.totalSales}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("New Sale") },
                icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("History") },
                icon = { Icon(Icons.Default.DateRange, contentDescription = null) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        when (selectedTab) {
            0 -> {
                Text("Tap item to sell:", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 8.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.inventory) { item ->
                        InventoryCard(item = item, onClick = { selectedItem = item })
                    }
                }
            }
            1 -> {
                Text("Recent Transactions:", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 8.dp))

                if (state.sales.isEmpty()) {
                    Text("No sales yet.", color = Color.Gray, modifier = Modifier.padding(top = 16.dp))
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(state.sales) { sale ->
                            SaleHistoryCard(sale = sale)
                        }
                    }
                }
            }
        }
    }

    if (selectedItem != null) {
        SellItemDialog(
            item = selectedItem!!,
            viewModel = viewModel,
            onDismiss = { selectedItem = null },
            onConfirm = { qty, finalPrice ->
                viewModel.insertSales(
                    itemID = selectedItem!!.id,
                    itemName = selectedItem!!.name,
                    quantity = qty,
                    price = finalPrice,
                    date = LocalDate.now().toString()
                )
                selectedItem = null
            }
        )
    }
}

@Composable
fun InventoryCard(item: InventoryEntity, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(item.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    "In Stock: ${item.currentQuantity}",
                    color = if(item.currentQuantity < 5) Color.Red else Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text("$${item.unitPrice}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SaleHistoryCard(sale: SalesEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(sale.itemName, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text("Qty: ${sale.quantity} | ${sale.date}", style = MaterialTheme.typography.bodySmall)
            }
            Text("+$${sale.price}", style = MaterialTheme.typography.titleMedium, color = Color(0xFF2E7D32))
        }
    }
}

@Composable
fun SellItemDialog(
    item: InventoryEntity,
    viewModel: POSViewModel,
    onDismiss: () -> Unit,
    onConfirm: (Int, Double) -> Unit
) {
    var quantityStr by remember { mutableStateOf("1") }
    val quantity = quantityStr.toIntOrNull() ?: 0
    val totalCost = viewModel.getTotalPrice(item.unitPrice, quantity)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sell ${item.name}") },
        text = {
            Column {
                OutlinedTextField(
                    value = quantityStr,
                    onValueChange = { quantityStr = it },
                    label = { Text("Quantity") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Unit Price:", color = Color.Gray)
                    Text("$${item.unitPrice}")
                }
                Spacer(modifier = Modifier.height(4.dp))
                Divider()
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total:", style = MaterialTheme.typography.titleMedium)
                    Text("$${totalCost}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(quantity, totalCost) },
                enabled = quantity > 0 && quantity <= item.currentQuantity
            ) {
                Text("Confirm Sale")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}