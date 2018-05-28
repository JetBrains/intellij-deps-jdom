package org.jdom.test.cases.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Comment;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.ProcessingInstruction;
import org.jdom.Text;
import org.jdom.output.XMLOutputter2;
import org.jdom.transform.JDOMResult;
import org.jdom.transform.JDOMSource;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestJDOMTransform {
	
	private static final void checkTransform(Document doc) {
	    XMLOutputter2 out = new XMLOutputter2();
	    String expect = out.outputString(doc);
	    
	    try {
	    
		    Transformer t = TransformerFactory.newInstance().newTransformer();
		    StringWriter sw = new StringWriter();
		    t.transform(new JDOMSource(doc), new StreamResult(sw));
		    JDOMResult res = new JDOMResult();
		    String intermediate = sw.toString() + "\n\n\n\n   ";
		    t.transform(new StreamSource(new StringReader(intermediate)), res);
		    Document redone = res.getDocument();
		    String actual =  out.outputString(redone);
		    assertEquals(expect, actual);
	    } catch (TransformerException te) {
	    	te.printStackTrace();
	    	fail (te.getMessage());
	    }
	}
	
    @Test
    public void testRootAttNS() {
    	Document doc = new Document();
    	Element e = new Element("root");
    	e.setAttribute(new Attribute("att", "val", Namespace.getNamespace("ans", "mynamespace")));
    	doc.addContent(e);
    	checkTransform(doc);
    }
    
	
	@Test
	public void testSimpleDocument() {
		checkTransform(new Document(new Element("root")));
	}

	@Test
	@Ignore //TODO DocType does not survive transform. Can it be tested? 
	public void testDocumentDocType() {
		DocType dt = new DocType("root");
		checkTransform(new Document(new Element("root"), dt));
	}

	@Test
	public void testComplexDocument() {
		// throw a lot of junk at the process... testing it all.
    	Document doc = new Document();
    	doc.addContent(new Comment("This is a document"));
    	doc.addContent(new ProcessingInstruction("jdomtest", ""));
    	Element root = new Element("root");
    	// messes up the ordering of things ... root.setAttribute(new Attribute("att", "val"));
    	root.setAttribute(new Attribute("att", "val", Namespace.getNamespace("ans", "attns")));
    	root.addContent(new Text(" "));
    	root.addContent(new Element("child", Namespace.getNamespace("nopfx")));
    	root.addContent(new CDATA(" cdata "));
    	root.addContent(new Comment("comment"));
    	Element otherchild = new Element("child", Namespace.getNamespace("cns", "childns"));
    	otherchild.addNamespaceDeclaration(Namespace.getNamespace("abc","oddns"));
    	Element grandchild = new Element("leaf", Namespace.getNamespace("abc", "oddns"));
    	// EntityRef will not survive transform 
    	// grandchild.addContent(new EntityRef("myref", "systemID"));
    	otherchild.addContent(grandchild);
    	root.addContent(otherchild);
    	doc.addContent(root);
		checkTransform(doc);

	}
	
}
