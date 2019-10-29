package craicoverflow89.kraql

import java.awt.event.KeyEvent
import javax.swing.*
import kotlin.system.exitProcess

class KraQLStudio: JFrame() {

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
                        // NOTE: render About dialog
                    }
                })
            })
        }

        // Create Content
        add(JPanel().apply {

            // TEMP LABEL
            add(JLabel("Content goes here"))
        })

        // Frame Visible
        setVisible(true)
    }

}

fun main() {

    KraQLStudio()

}