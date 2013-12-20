package org.nodeclipse.ui.contentassist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Image;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nodeclipse.ui.Activator;
import org.nodeclipse.ui.util.Constants;
import org.nodeclipse.ui.util.NodeclipseConsole;


/**
 * TODO make this class JSON independent
 * TODO sorted proposals
 * 
 * @author Lamb Gao
 * @author Paul Verest
 * 
 */
public class NodeContentAssistant implements IContentAssistProcessor {

    public static Image METHOD = Activator.getImageDescriptor(Constants.METHOD_ICON).createImage();
    public static Image CLASS = Activator.getImageDescriptor(Constants.CLASS_ICON).createImage();

    public String getInputString(IDocument doc, int offset) {
        StringBuffer buf = new StringBuffer();
        while (true) {
            try {
                char charOffset = doc.getChar(--offset);
                if (Character.isWhitespace(charOffset))
                    break;
                buf.append(charOffset);
            } catch (BadLocationException e) {
                break;
            }
        }
        return buf.reverse().toString();
    }
    
//    private void addCompletionProposalFromNodejsSources(
//			List<CompletionProposal> list, String input, int offset) {
//        try {
//            for (int i = 0; i < ContentFromSources.METHODS.length(); i++) {
//                JSONObject method = (JSONObject) ContentFromSources.METHODS.get(i);
//                String trigger = method.getString("textRaw");
//                if (trigger != null && trigger.startsWith(input)) {
//                    int length = input.length();
//                    list.add(new CompletionProposal(trigger, offset - length, length, trigger.length(), 
//                    		METHOD, null, null, method.getString("desc") )); 
//                    		//method.getString("name")
//                }
//            }
//        } catch (JSONException e) {
//            //e.printStackTrace();
//        	NodeclipseConsole.write(e.getLocalizedMessage()+"\n");
//        }		
//	}

    private void addCompletionProposalFromNodejsSources(
			List<CompletionProposal> list, String input, int offset) {
        int length = input.length();
        // modules30: timers(m8), module, addons, util(m13), Events(c1), domain(m1)(c1), buffer(c2), stream(c4), crypto(m18)(c7), 
        // tls_(ssl)(m5)(c4), stringdecoder(c1), fs(m67)(c4), path(m7), net(m10)(c2), dgram(m1)(c1), dns(m10), http(m4)(c4), https(m3)(c2), 
        // url(m3), querystring(m2), punycode(m4), readline(m1)(c1), repl(m1), vm(m5)(c1), child_process(m4)(c1), assert(m11), tty(m2)(c2), zlib(m14)(c8), os(m13), cluster(m3)(c1)
        try {
        	JSONObject NodejsContext = ContentFromSources.NodejsContext;
			JSONArray modules = NodejsContext.getJSONArray("modules");
			log("modules"+modules.length()+" ");
			for (int i = 0; i < modules.length(); i++) {
				JSONObject module = (JSONObject) modules.get(i);
				String moduleName = module.getString("name");
				debug( ", "+moduleName);

				if (module.has("methods")) {
					JSONArray methods = module.getJSONArray("methods");
					debug("(m"+methods.length()+")");
					for (int j = 0; j < methods.length(); j++) {
						JSONObject method = (JSONObject) methods.get(j);
						// example: "textRaw": "http.createServer([requestListener])","type": "method","name": "createServer",
						String trigger = method.getString("textRaw");
						if (trigger != null && trigger.startsWith(input)) {
							String name = method.getString("name");
							String desc = formatedName(name,trigger)+method.getString("desc");
							list.add(new CompletionProposal(trigger, offset - length, length, trigger.length(), 
									METHOD, trigger, null, desc));
						}
					}
				}

				if (module.has("classes")){
	                JSONArray classes = module.getJSONArray("classes");
	                debug("(c"+classes.length()+")");
	                for (int j = 0; j < classes.length(); j++) {
	                    JSONObject clazz = (JSONObject) classes.get(j);
	                    // example: "textRaw": "Class: Domain","type": "class","name": "Domain"
	                    String trigger = clazz.getString("name");
	                    if (!trigger.startsWith(moduleName)) {
	                    	trigger=moduleName+'.'+trigger;
	                    }
	                    if (trigger != null && trigger.startsWith(input)) {
	                    	//String name = clazz.getString("name");
	                    	String desc = formatedName(trigger,clazz.getString("textRaw"))+clazz.getString("desc");
	                        list.add(new CompletionProposal(trigger, offset - length, length, trigger.length(), 
	                        		CLASS, trigger, null, desc )); 
	                    }
	                }
                }
                
        	}
        } catch (JSONException e) {
        	log(e.getLocalizedMessage()+"\n"+e);
        }		
	}
    
    private String formatedName(String name) {
		return "<b>"+name+"</b><br/>";
	}
    private String formatedName(String name, String trigger) {
		return formatedName(name)+"<code>"+trigger+"</code><br/>";
	}

	public void addCompletionProposalFromCompletionJson(
    		List<CompletionProposal> list, String input, int offset) {
        //List<CompletionProposal> list = new ArrayList<CompletionProposal>();
        int length = input.length();
        try {
            for (int i = 0; i < ContentProvider.COMPLETIONS.length(); i++) {
                JSONObject method = (JSONObject) ContentProvider.COMPLETIONS.get(i);
                String trigger = method.getString("trigger");
                if (trigger != null && trigger.startsWith(input)) {
                    list.add(new CompletionProposal(trigger, offset - length, length, trigger.length(), 
                    		//METHOD
                    		null, null, null, null));
                }
            }
        } catch (JSONException e) {
        	log(e.getLocalizedMessage()+"\n");
        }
        //return list;
    }

    @Override
    public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
        IDocument doc = viewer.getDocument();
        String inputString = getInputString(doc, offset);
        //debug(inputString+"\n");
        //List<CompletionProposal> list;
        List<CompletionProposal> list = new ArrayList<CompletionProposal>();
        //list = getCompletionProposalFromCompletionJson(inputString , offset);
        addCompletionProposalFromNodejsSources(list, inputString , offset);
        addCompletionProposalFromCompletionJson(list, inputString , offset);
        return (CompletionProposal[]) list.toArray(new CompletionProposal[list.size()]);
    }
    
    @Override
    public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public char[] getCompletionProposalAutoActivationCharacters() {
        // TODO Preferences
        return ".abcdefghijklmnopqrstuvwxyz".toCharArray();
    }

    @Override
    public char[] getContextInformationAutoActivationCharacters() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getErrorMessage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IContextInformationValidator getContextInformationValidator() {
        // TODO Auto-generated method stub
        return null;
    }

    private static void debug(String s){
    	//NodeclipseConsole.write(s);
    }
    private static void log(String s){
    	NodeclipseConsole.write(s);
    }
}
