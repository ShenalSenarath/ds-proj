/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ac.mrt.cse.dsproj;

/**
 *
 * @author dewmal
 */
public class StartPage extends javax.swing.JFrame {

    /**
     * Creates new form StartPage
     */

    
    public StartPage() {
        initComponents();
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        username = new javax.swing.JTextField();
        portNumber = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        hostIp = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        hostPort = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameActionPerformed(evt);
            }
        });

        jLabel1.setText("Username");

        jLabel2.setText("Port");

        jButton1.setText("JOIN");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setText("Server Ip");

        hostIp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hostIpActionPerformed(evt);
            }
        });

        jLabel4.setText("Server Port");

        hostPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hostPortActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(60, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(65, 65, 65)
                                .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(portNumber, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(hostIp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(hostPort, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(131, 131, 131))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(portNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(hostIp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(hostPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void usernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usernameActionPerformed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
                    // TODO add your handling code here:
    }//GEN-LAST:event_jButton1MouseClicked

    private void hostIpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hostIpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hostIpActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String args[] = new String[4];
        args[0] = portNumber.getText();
        args[1] = hostIp.getText();
        args[2] = hostPort.getText();
        args[3] = username.getText();
       // this.server.startNode(args);
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void hostPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hostPortActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hostPortActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(StartPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(StartPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(StartPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(StartPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new StartPage().setVisible(true);
//            }
//        });
//    }
    //Need to implement methods to read files names and queries and add it to a list.
//    private static String[] all_files = {
//            "Adventures of Tintin",
//            "Jack and Jill",
//            "Glee",
//            "The Vampire Diarie",
//            "King Arthur",
//            "Windows XP",
//            "Harry Potter",
//            "Kung Fu Panda",
//            "Lady Gaga",
//            "Twilight",
//            "Windows 8",
//            "Mission Impossible",
//            "Turn Up The Music",
//            "Super Mario",
//            "American Pickers",
//            "Microsoft Office 2010",
//            "Happy Feet",
//            "Modern Family",
//            "American Idol",
//            "Hacking for Dummies"	};
//
//    
//    public static String[] getRandomFiles(){
//        Random r = new Random();
//        //choose randomly between 3,4,5
//        int file_count = r.nextInt(3) + 3 ; //random.nextInt(max - min + 1) + min
//        String []fileList = new String[file_count];
//        List<String> all_files_list = new LinkedList<String>(Arrays.asList(all_files));
//        //List<String> all_files_list = Arrays.asList(all_files); //String [] to List
//
//        for( int i=0; i < file_count; i++){
//            fileList[i] = all_files_list.remove(r.nextInt(all_files_list.size()-1) );
//        }
//        return fileList;
//    }
//
//    public static void startNode(String args[]){
//        int attempt_count = 0;
//        try{
//            System.out.println("Initializing Node.....");
//            int port = Integer.parseInt(args[0]);
//            Node thisNode = new Node(port);
//            System.out.println("Registering Node with the Bootstrap Server....");
//
//            //bootstrap server ip address and port
//            BootstrapServer.startConnection(getByName(args[1]),Integer.parseInt(args[2]));
//            BootstrapServer server=BootstrapServer.getInstance();
//            boolean success = false;
//
//            while(attempt_count < 3){
//                if(server.registerNode(thisNode, args[3])){
//                    System.out.println("Node Successfully registered with the Bootstrap Server. ");
//                    success = true;
//                    //randomly add fiels
//                    thisNode.setFileList(getRandomFiles());
//                    
//                    break;
//                }else{
//                    Thread.sleep(5000);
//                    attempt_count++;
//                    System.out.print("Attempt " + attempt_count + " :");
//                    System.out.println("Error registering node");
//                    //server.unregisterNode(thisNode, args[3]);
//                    //need to make sure at what stage we need to unregister. perhaps send some int code relevant to each stage.
//                    //e.g if request was successful and only the reading response got issues then we might want to unregister.
//                }
//            }
//
//            if(!success){
//                System.exit(1);
//            }
//
//            /*
//            if(server.registerNode(thisNode, args[3])){
//                System.out.println("Node Successfully registered with the Bootstrap Server. ");
//            }else{
//            	attempt_count++;
//                System.out.println("Error registering node");
//                System.exit(1);
//            }*/
//
//            System.out.println("UDP Server for the Node initializing....");
//            Thread thread = new Thread(thisNode);
//            thread.start();
//            Thread.sleep((long)1000);
//
//            System.out.println("Node joining with neighbours...");
//            thisNode.joinNeighbours();
//
//            //File adding part done differently
//            
////            System.out.println("Add the titles in this Node:");
////            System.out.println("Syntax: title1,title2,title3");
//
////            Scanner scanIn = new Scanner(System.in);
//////            String titles = scanIn.nextLine();
////
//////            thisNode.setFileList(titles.split(","));
////            
////            
////            System.out.println("Enter A File name to search ");
////            String fileName = scanIn.nextLine();
////            
////            scanIn.close();
////            thisNode.initiateSearch(fileName);
//            
//
//            //thisNode.initiateSearch();
//
//        }catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
//            System.out.println("Please enter the details in the correct format to start the Node.");
//            System.out.println("Syntax: java StartNode NodePort BootstrapServerIP BootstrapServerPort ");
//            System.out.println("Eg: java StartNode 7001 127.0.0.1 8000 NodeShenal");
//            System.exit(10);
//        } catch (UnknownHostException e) {
//            System.out.println("Cannot determine the host address.");
//            System.exit(10);
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            System.out.println("Network Error.");
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField hostIp;
    private javax.swing.JTextField hostPort;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField portNumber;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}