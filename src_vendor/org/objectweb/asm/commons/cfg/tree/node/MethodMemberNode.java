package org.objectweb.asm.commons.cfg.tree.node;

import org.objectweb.asm.commons.cfg.tree.NodeTree;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

/**
 * @author Tyler Sedlar
 */
public class MethodMemberNode extends ReferenceNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8738867094685669268L;

	public MethodMemberNode(NodeTree tree, AbstractInsnNode insn, int collapsed, int producing) {
		super(tree, insn, collapsed, producing);
	}

    public MethodInsnNode min() {
        return (MethodInsnNode) insn();
    }
}
