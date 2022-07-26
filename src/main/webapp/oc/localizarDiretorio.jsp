<%@page import="javax.swing.JFileChooser"%>
<%                  
					 JFileChooser filechoose = new JFileChooser();     
					 filechoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

					 filechoose.showOpenDialog(null); 
					 session.setAttribute("caminho do arquivo",filechoose.getSelectedFile().toString());
					
					 %>
