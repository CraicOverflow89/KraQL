package craicoverflow89.kraql.portal

import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.*

interface KraQLPortalDialog

class KraQLPortalDialogAbout(application: KraQLPortal): JDialog(application, "About", true), KraQLPortalDialog {

    init {

        // Dialog Properties
        setSize(200, 200)
        isResizable = false
        setLocationRelativeTo(application)

        // Create Content
        layout = GridLayout(3, 1).apply {
            setSize(200, 200)
            add(JLabel("KraQL Portal"))
            add(JLabel("Version ${application.getVersion()}"))
            add(JButton("OK").apply {
                addActionListener {
                    this@KraQLPortalDialogAbout.hide()
                }
            }, BorderLayout.EAST)
        }
    }

}