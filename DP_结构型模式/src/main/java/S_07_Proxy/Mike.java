package S_07_Proxy;

interface CheatingWife {// think about what this kind of women can do

    void seduceMan();// such as eye contact with men

    void happyWithMan();// happy what? You know that.
}

class HouseWifeOne implements CheatingWife {

    public void seduceMan() {
        System.out.println("HouseWifeOne secude men, such as making some sexy poses ...");
    }

    public void happyWithMan() {
        System.out.println("HouseWifeOne is happy with man ...");
    }
}

class BusinessAgent implements CheatingWife {
    private CheatingWife cheatingWife;

    public BusinessAgent() {
        this.cheatingWife = new HouseWifeOne();
    }

    public BusinessAgent(CheatingWife cheatingWife) {
        this.cheatingWife = cheatingWife;
    }

    public void seduceMan() {
        this.cheatingWife.seduceMan();
    }

    public void happyWithMan() {
        this.cheatingWife.happyWithMan();
    }

}

// see? it looks that agent/proxy is doing
public class Mike {

    public static void main(String[] args) {
        BusinessAgent businessAgent = new BusinessAgent();
        businessAgent.seduceMan();
        businessAgent.happyWithMan();
    }
}