package bitmmo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nullbool.api.util.ClassStructure;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import me.themallard.bitmmo.api.analysis.util.LdcContains;

public class LdcContainsTest implements Opcodes {

	// there is no reason to test all of the other methods in LdcContainsTest,
	// because ClassContains will call them all.

	@Test
	public void testClassContains() {
		String valid = "hey there brown cow";
		String invalid = "iusgdfhjdfughdfg";

		ClassWriter cw = new ClassWriter(0);
		MethodVisitor mv;

		cw.visit(49, ACC_PUBLIC + ACC_SUPER, "Hello", null, "java/lang/Object", null);

		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitLdcInsn(valid);
			mv.visitInsn(RETURN);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		cw.visitEnd();

		ClassNode cn = ClassStructure.create(cw.toByteArray());

		assertEquals(true, LdcContains.ClassContains(cn, valid));
		assertEquals(false, LdcContains.ClassContains(cn, invalid));
	}

}
