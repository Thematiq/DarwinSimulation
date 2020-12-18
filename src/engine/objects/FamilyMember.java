package engine.objects;

import java.util.ArrayList;
import java.util.List;

public class FamilyMember {
    List<FamilyMember> children = new ArrayList<>();

    public FamilyMember() {

    }

    public int getNoChildren() {
        return this.children.size();
    }

    public int getNoDescendants() {
        if (this.children.size() == 0) {
            return 0;
        } else {
            int totalDescendants = 0;
            for(FamilyMember child : this.children) {
                totalDescendants += child.getNoDescendants();
            }
            return totalDescendants + this.children.size();
        }
    }
}
