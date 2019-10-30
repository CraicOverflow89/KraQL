package craicoverflow89.kraql.portal

import java.awt.BorderLayout
import java.awt.event.KeyEvent
import javax.swing.*
import kotlin.system.exitProcess

class KraQLPortal: JFrame() {

    private val dialogAbout = KraQLPortalDialogAbout(this)

    init {

        // Frame Properties
        setTitle("KraQL")
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(1000, 600)
        // NOTE: need to set this to window size
        //       and maximised by default
        setLocationRelativeTo(null)

        // Create Menu
        jMenuBar = JMenuBar().apply {

            // File Menu
            add(JMenu("File").apply {
                mnemonic = KeyEvent.VK_F

                // File Exit
                add(JMenuItem("Exit").apply {
                    mnemonic = KeyEvent.VK_E
                    addActionListener {
                        exitProcess(0)
                    }
                })
            })

            // Help Menu
            add(JMenu("Help").apply {
                mnemonic = KeyEvent.VK_H

                // Help About
                add(JMenuItem("About").apply {
                    mnemonic = KeyEvent.VK_A
                    addActionListener {
                        dialogAbout.show()
                    }
                })
            })
        }

        // Create Explorer
        contentPane.add(JLabel("Object Explorer"), BorderLayout.LINE_START)

        // Create Content
        contentPane.add(JPanel().apply {

            // TEMP LABEL
            add(JLabel("Content View"))
        }, BorderLayout.CENTER)

        // Create Status
        contentPane.add(JLabel("Status Bar"), BorderLayout.PAGE_END)

        // Frame Visible
        setVisible(true)
    }

    fun getVersion() = "0.0.1"
    // NOTE: come back to this

}