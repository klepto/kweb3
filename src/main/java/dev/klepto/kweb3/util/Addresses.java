package dev.klepto.kweb3.util;

import dev.klepto.kweb3.abi.type.Address;
import io.github.classgraph.ClassGraph;
import lombok.Getter;
import lombok.val;

import java.util.HashMap;
import java.util.Map;

import static dev.klepto.kweb3.util.Logging.log;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Addresses {

    @Getter(lazy = true)
    private final static Map<Address, String> domains = findDomains();

    public static String domain(Address address) {
        return getDomains().getOrDefault(address, address.toString());
    }

    private static Map<Address, String> findDomains() {
        val domains = new HashMap<Address, String>();
        val type = Address.class.getName();
        try (val scanResult = new ClassGraph().enableFieldInfo().scan()) {
            for (val classInfo : scanResult.getAllClasses()) {
                for (val fieldInfo : classInfo.getFieldInfo()) {
                    if (!fieldInfo.isStatic() || !fieldInfo.isPublic() || !fieldInfo.isFinal()) {
                        // Only load constant static fields.
                        continue;
                    }

                    if (!fieldInfo.getTypeDescriptor().toString().equals(type)) {
                        // Only load Address type constants.
                        continue;
                    }

                    try {
                        val field = fieldInfo.loadClassAndGetField();
                        val domain = field.getName();
                        val address = (Address) field.get(null);

                        if (domains.containsKey(address) && !domains.get(address).equals(domain)) {
                            log().error("Domain collision detected: {}#{}", classInfo.getName(), fieldInfo.getName());
                            continue;
                        }

                        domains.put(address, domain);
                    } catch (Exception e) {
                        log().error("Couldn't parse address domain {}#{}.", classInfo.getName(), fieldInfo.getName());
                    }
                }
            }
        }
        return domains;
    }

}
