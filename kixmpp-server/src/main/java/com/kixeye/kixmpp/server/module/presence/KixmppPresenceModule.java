package com.kixeye.kixmpp.server.module.presence;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.Collections;
import java.util.List;

import org.jdom2.Element;

import com.kixeye.kixmpp.server.KixmppServer;
import com.kixeye.kixmpp.server.KixmppStanzaHandler;
import com.kixeye.kixmpp.server.module.KixmppModule;
import com.kixeye.kixmpp.server.module.bind.KixmppBindModule;

/**
 * Handles MUC features.
 * 
 * @author ebahtijaragic
 */
public class KixmppPresenceModule implements KixmppModule {
	public static AttributeKey<String> PRESENCE = AttributeKey.valueOf("PRESENCE");
	
	private KixmppServer server;
	
	/**
	 * @see com.kixeye.kixmpp.server.module.KixmppModule#install(com.kixeye.kixmpp.server.KixmppServer)
	 */
	public void install(KixmppServer server) {
		this.server = server;
		
		this.server.getHandlerRegistry().register("presence", null, PRESENCE_HANDLER);
	}

	/**
	 * @see com.kixeye.kixmpp.server.module.KixmppModule#uninstall(com.kixeye.kixmpp.server.KixmppServer)
	 */
	public void uninstall(KixmppServer server) {
		this.server.getHandlerRegistry().unregister("presence", null, PRESENCE_HANDLER);
	}

	/**
	 * @see com.kixeye.kixmpp.server.module.KixmppModule#getFeatures()
	 */
	public List<Element> getFeatures() {
		return Collections.emptyList();
	}
	
	private KixmppStanzaHandler PRESENCE_HANDLER = new KixmppStanzaHandler() {
		/**
		 * @see com.kixeye.kixmpp.server.KixmppStanzaHandler#handle(io.netty.channel.Channel, org.jdom2.Element)
		 */
		public void handle(Channel channel, Element stanza) {
			if (stanza.getAttributeValue("to") == null) {
				Element presence = new Element("presence");
				presence.setAttribute("from", channel.attr(KixmppBindModule.JID).get().toString());
				presence.setAttribute("to", channel.attr(KixmppBindModule.JID).get().toString());
				
				channel.writeAndFlush(presence);
			}
		}
	};
}
