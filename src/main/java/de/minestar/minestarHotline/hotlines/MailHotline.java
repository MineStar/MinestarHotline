/*
 * Copyright (C) 2012 MineStar.de 
 * 
 * This file is part of MinestarHotline.
 * 
 * MinestarHotline is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * MinestarHotline is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MinestarHotline.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.minestarHotline.hotlines;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import de.minestar.minestarHotline.core.Core;
import de.minestar.minestarlibrary.config.MinestarConfig;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class MailHotline implements Hotline {

    private Session session;
    private InternetAddress from;

    // THE SUPPORTER MAIL ACCOUNTS
    private Map<String, InternetAddress> mailAccounts = new HashMap<String, InternetAddress>();

    public MailHotline(File dataFolder) {
        this.loadSettings(dataFolder);
        this.loadMailAccounts(dataFolder);
    }

    @Override
    public void sendMessage(String callerName, String message) {

        // SUBJECT OF THE MAIL
        String subject = "MinestarHotline - '" + callerName + "' braucht Hilfe";
        // PREPARE TEXT
        StringBuilder sBuilder = new StringBuilder("Hallo Supporter,\n\n");
        sBuilder.append("Spieler '" + callerName + "' hat die Hotline benutzt und hat folgende Anfrage:\n\"");
        sBuilder.append(message);
        sBuilder.append("\"\n\nMinestarHotline");

        // SEND MAIL
        this.sendMail(subject, sBuilder.toString());
    }

    @Override
    public void registerSupporter(String supportName, String contactInformation) {
        if (this.isValidAddress(contactInformation)) {
            try {
                mailAccounts.put(supportName, new InternetAddress(contactInformation));
            } catch (Exception e) {
                ConsoleUtils.printException(e, Core.NAME, "Can't create an InternetAddress for " + contactInformation + "!");
            }
        } else
            ConsoleUtils.printError(Core.NAME, "The E-Mail Address '" + contactInformation + "' isn't valid!");
    }

    @Override
    public void unregisterSupporter(String suppportName, String contactInformation) {
        mailAccounts.remove(suppportName);
    }

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    public boolean isValidAddress(String mailAddress) {
        return EMAIL_PATTERN.matcher(mailAddress).matches();
    }

    private void loadSettings(File dataFolder) {
        File configFile = new File(dataFolder, "/mail/mailConfig.yml");
        try {
            MinestarConfig config = new MinestarConfig(configFile);
            // STANDARDIZED PROPERTIES TO TALK WITH SMTP SERVER
            Properties properties = System.getProperties();
            // HOST
            properties.setProperty("mail.smtp.host", config.getString("smtp.host"));
            // PORT
            properties.setProperty("mail.smtp.port", config.getString("smtp.port"));
            // HAS AN AUTHENTIFACTION
            properties.setProperty("mail.smtp.auth", "true");
            // CREATE SESSION
            this.session = Session.getDefaultInstance(properties, new MailAuthenticator(config.getString("smtp.username"), config.getString("smtp.password")));
            // GET ADDRESSES
            this.from = new InternetAddress(config.getString("smtp.from"));
        } catch (Exception e) {
            ConsoleUtils.printException(e, Core.NAME, "Can't initiate E-Mail settings!");
        }
    }

    private void loadMailAccounts(File dataFolder) {
        File accountFile = new File(dataFolder, "/mail/accounts.txt");
        if (!accountFile.exists()) {
            ConsoleUtils.printWarning(Core.NAME, "File " + accountFile + " not found! No Mail Accounts are registered!");
            return;
        }
        try {
            BufferedReader bReader = new BufferedReader(new FileReader(accountFile));
            // SUPPORT NAME AND MAIL ARE SEPERATED BY TWO ;
            Pattern splitter = Pattern.compile(";;");

            // TEMP
            String line = "";
            String[] split = null;

            // READ FILE
            while ((line = bReader.readLine()) != null) {
                // IGNORE EMPTY LINES
                if (line.trim().isEmpty())
                    continue;

                // SPLIT BY ;;
                split = splitter.split(line);
                // TRY TO REGISTER THE SUPPORTER
                this.registerSupporter(split[0], split[1]);
            }

            ConsoleUtils.printInfo(Core.NAME, "Loaded " + mailAccounts.size() + " mail accounts");
        } catch (Exception e) {
            ConsoleUtils.printException(e, Core.NAME, "Can't load mail accounts from " + accountFile);
        }

    }
    // PRIVATE CLASS TO HOLD PASSWORD AND USER
    private class MailAuthenticator extends Authenticator {
        private String user;
        private String password;

        public MailAuthenticator(String user, String password) {
            this.user = user;
            this.password = password;
        }

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, password);
        }
    }

    // SENDING THE MAIL
    private boolean sendMail(String subject, String message) {
        MimeMessage msg = new MimeMessage(this.session);
        try {
            msg.setFrom(this.from);
            msg.setSubject(subject);
            msg.setText(message);
            Collection<InternetAddress> tempCollection = this.mailAccounts.values();
            InternetAddress[] recipients = (InternetAddress[]) tempCollection.toArray(new InternetAddress[tempCollection.size()]);
            msg.setRecipients(Message.RecipientType.TO, recipients);
            Transport.send(msg);
            return true;
        } catch (Exception e) {
            ConsoleUtils.printException(e, Core.NAME, "Can't send mail!");
            return false;
        }
    }
}
