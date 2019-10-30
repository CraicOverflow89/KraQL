package craicoverflow89.kraql.portal

import java.awt.Color
import javax.swing.*

interface KraQLPortalDialog

class KraQLPortalDialogAbout(application: KraQLPortal): JDialog(application, "About", true), KraQLPortalDialog {

    init {

        // Dialog Properties
        setSize(250, 200)
        isResizable = false
        setLocationRelativeTo(application)

        // Create Content
        add(JPanel().apply {
            background = Color.BLACK
            add(JLabel(ImageIcon((object {}.javaClass.getResource("/portal/logo.png"))).apply {
                setSize(200, 110)
            }))
            add(JLabel("KraQL Portal"))
            add(JLabel("Version ${application.getVersion()}"))
            add(JButton("OK").apply {
                addActionListener {
                    this@KraQLPortalDialogAbout.hide()
                }
            })
        })
    }

}