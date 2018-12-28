package khsu.org.coal

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

typealias Callback = (DialogInterface, Int) -> Unit

class MainActivity : AppCompatActivity() {

    private var selectedIndexProvider: Int = -1
    private var selectedIndexCoal: Int = -1

    private val providers: List<Provider> = Providers().getProviders()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvProvider.setOnClickListener {
            val options = providers.map { provider -> provider.name }.toTypedArray()
            val dialog = createDialog(
                context = this,
                options = options,
                title = "Поставщик угля",
                selectedIndex = selectedIndexProvider,
                onSelectCallback = { _, i -> selectedIndexProvider = i },
                onPositiveCallback = { _, _ ->
                    if (selectedIndexProvider != -1) {
                        tvProvider.text = "Поставщик угля: ${options[selectedIndexProvider]}"
                    }
                    if (selectedIndexCoal != -1) {
                        selectedIndexCoal = -1
                        tvCoal.text = "Марка угля:"
                        tvCoalPrice.text = "Цена за тонну:"
                    }
                }
            )
            dialog.show()
        }

        tvCoal.setOnClickListener {
            val optionsList: MutableList<String> = mutableListOf()
            if (selectedIndexProvider < 0) {
                providers.map { Provider -> Provider.coals.map { coal -> optionsList.add("${coal.name} (${coal.price})") } }
            } else {
                providers[selectedIndexProvider].coals.map { coal -> optionsList.add("${coal.name} (${coal.price})") }
            }
            val options = optionsList.toTypedArray()
            val dialog = createDialog(
                this,
                options,
                "Марка угля",
                selectedIndexCoal,
                { _, i -> selectedIndexCoal = i },
                { _, _ ->
                    if (selectedIndexProvider == -1) findIndices(options)
                    if (selectedIndexCoal != -1) {
                        val coal = providers[selectedIndexProvider].coals[selectedIndexCoal]
                        tvCoal.text = "Марка угля: ${coal.name}"
                        tvCoalPrice.text = "Цена за тонну: ${coal.price}"
                        tvProvider.text = "Поставщик угля: ${providers[selectedIndexProvider].name}"
                        if (getValue(tvWeight).isNotEmpty()) {
                            updatePrice()
                        }
                    }
                }
            )
            dialog.show()
        }

        tvWeight.setOnClickListener {
            val dialog = createDialogText(
                this,
                "Вес",
                tvWeight,
                "Сколько в тоннах: "
            ) { _, _ ->
                if (getValue(tvCoal).isNotEmpty()) {
                    updatePrice()
                }
            }

            dialog.show()
        }

        tvAddress.setOnClickListener {
            createDialogText(this, "Адрес", tvAddress, "Адрес: ").show()
        }
    }

    private fun getValue(textView: TextView): String =
        textView.text.split(":")[1].trim()

    private fun updatePrice() {
        val weight = getValue(tvWeight).toInt()
        val price = providers[selectedIndexProvider].coals[selectedIndexCoal].price
        tvPrice.text = "Цена: ${weight * price} рублей"
    }
    private fun findIndices(options: Array<String>){
        val options = options
        if (selectedIndexCoal == -1) return
        for (provider in providers) {
            for (coal in provider.coals) {
                if (coal.name == options[selectedIndexCoal].split("(")[0].trim()) {
                    selectedIndexCoal = provider.coals.indexOf(coal)
                    selectedIndexProvider = providers.indexOf(provider)
                    return
                }
            }
        }
    }

}
