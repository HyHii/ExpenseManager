package com.example.expensemanager.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.expensemanager.utils.CategoryGroup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryGroupDropdown(
    categoryGroups: List<CategoryGroup>,
    selectedGroup: String,
    selectedSubCategory: String,
    onGroupSelected: (String) -> Unit,
    onSubCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var groupExpanded by remember { mutableStateOf(false) }
    var subExpanded by remember { mutableStateOf(false) }

    val currentGroup = categoryGroups.find { it.name == selectedGroup }
        ?: categoryGroups.first()

    Column(
        modifier = modifier
    ) {
        ExposedDropdownMenuBox(
            expanded = groupExpanded,
            onExpandedChange = {
                groupExpanded = !groupExpanded
            }
        ) {
            OutlinedTextField(
                value = selectedGroup,
                onValueChange = {},
                readOnly = true,
                label = { Text("Main Category") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = groupExpanded)
                },
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = groupExpanded,
                onDismissRequest = {
                    groupExpanded = false
                }
            ) {
                categoryGroups.forEach { group ->
                    DropdownMenuItem(
                        text = { Text(group.name) },
                        onClick = {
                            onGroupSelected(group.name)
                            onSubCategorySelected(group.subCategories.first())
                            groupExpanded = false
                        }
                    )
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = subExpanded,
            onExpandedChange = {
                subExpanded = !subExpanded
            }
        ) {
            OutlinedTextField(
                value = selectedSubCategory,
                onValueChange = {},
                readOnly = true,
                label = { Text("Sub Category") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = subExpanded)
                },
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = subExpanded,
                onDismissRequest = {
                    subExpanded = false
                }
            ) {
                currentGroup.subCategories.forEach { subCategory ->
                    DropdownMenuItem(
                        text = { Text(subCategory) },
                        onClick = {
                            onSubCategorySelected(subCategory)
                            subExpanded = false
                        }
                    )
                }
            }
        }
    }
}