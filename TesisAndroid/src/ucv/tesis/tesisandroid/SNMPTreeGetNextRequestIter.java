package ucv.tesis.tesisandroid;
import java.util.Iterator;

public class SNMPTreeGetNextRequestIter<T> implements Iterator<SNMPTreeGetNextRequest<T>>{

	        enum ProcessStages {
	                ProcessParent, ProcessChildCurNode, ProcessChildSubNode
	        }

	        private SNMPTreeGetNextRequest<T> treeNode;

	        public SNMPTreeGetNextRequestIter(SNMPTreeGetNextRequest<T> treeNode) {
	                this.treeNode = treeNode;
	                this.doNext = ProcessStages.ProcessParent;
	                this.childrenCurNodeIter = treeNode.children.iterator();
	        }

	        private ProcessStages doNext;
	        private SNMPTreeGetNextRequest<T> next;
	        private Iterator<SNMPTreeGetNextRequest<String>> childrenCurNodeIter;
	        private Iterator<SNMPTreeGetNextRequest<String>> childrenSubNodeIter;

	        @Override
	        public boolean hasNext() {

	                if (this.doNext == ProcessStages.ProcessParent) {
	                        this.next = this.treeNode;
	                        this.doNext = ProcessStages.ProcessChildCurNode;
	                        return true;
	                }

	                if (this.doNext == ProcessStages.ProcessChildCurNode) {
	                        if (childrenCurNodeIter.hasNext()) {
	                        		SNMPTreeGetNextRequest<String> childDirect = childrenCurNodeIter.next();
	                                childrenSubNodeIter = childDirect.iterator();
	                                this.doNext = ProcessStages.ProcessChildSubNode;
	                                return hasNext();
	                        }

	                        else {
	                                this.doNext = null;
	                                return false;
	                        }
	                }
	                
	                if (this.doNext == ProcessStages.ProcessChildSubNode) {
	                        if (childrenSubNodeIter.hasNext()) {
	                                this.next = (SNMPTreeGetNextRequest<T>) childrenSubNodeIter.next();
	                                return true;
	                        }
	                        else {
	                                this.next = null;
	                                this.doNext = ProcessStages.ProcessChildCurNode;
	                                return hasNext();
	                        }
	                }

	                return false;
	        }

	        @Override
	        public SNMPTreeGetNextRequest<T> next() {
	                return this.next;
	        }

	        @Override
	        public void remove() {
	                throw new UnsupportedOperationException();
	        }
	        
}
