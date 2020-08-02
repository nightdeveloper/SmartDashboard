package com.github.nightdeveloper.smartdashboard.controller;

import com.github.nightdeveloper.mdns_explorer.dns.Domain;
import com.github.nightdeveloper.mdns_explorer.sd.Instance;
import com.github.nightdeveloper.mdns_explorer.sd.Query;
import com.github.nightdeveloper.mdns_explorer.sd.Service;
import com.github.nightdeveloper.smartdashboard.common.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.*;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DiscoveryController {

    final static Logger logger = LogManager.getLogger(DiscoveryController.class);

    private static List<InetAddress> getLocalAddresses() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

        List<InetAddress> result = new ArrayList<>();
        while (networkInterfaces.hasMoreElements()) {
            for (InterfaceAddress interfaceAddress : networkInterfaces.nextElement().getInterfaceAddresses()) {
                result.add(interfaceAddress.getAddress());
            }
        }

        return result;
    }

    private static void logAndJoin(List<String> result, String str) {
        logger.info(str);
        result.add(str);
    }

    @RequestMapping(value = Constants.ENDPOINT_DISCOVERY, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView discovery(Principal principal) {

        logger.info("started discovery " + principal.getName());

        ModelAndView modelAndView = new ModelAndView("discovery");
        Map<String, Object> model = modelAndView.getModel();

        List<String> result = new ArrayList<>();

        try {
            String hostname = InetAddress.getLocalHost().getHostName();

            logAndJoin(result, "Using hostname " + hostname);

            List<InetAddress> addresses = getLocalAddresses();

            for (InetAddress address : addresses) {

                logAndJoin(result, "querying interface " + address.getHostAddress());

                Service discoveryService = Service.fromName(Service.SERVICE_QUERY);
                Query serviceQuery = Query.createFor(discoveryService, Domain.LOCAL);
                Set<Instance> serviceInstances = serviceQuery.runOnce(address);
                logAndJoin(result, "got count " + serviceInstances.size());

                List<String> serviceNames = serviceInstances.stream().map(Instance::getFullName)
                        .sorted()
                        .distinct()
                        .collect(Collectors.toList());


                for (String serviceName : serviceNames) {
                    logAndJoin(result, "Discovered service " + serviceName + ":");

                    Query instanceQuery = Query.createFor(Service.fromName(serviceName), Domain.LOCAL);
                    Set<Instance> instances = instanceQuery.runOnce(address);

                    instances.forEach(instance -> logAndJoin(result, " - ["

                            + instance.getAddresses().stream()
                            .map(inetAddress ->
                                    inetAddress.getHostName().equals(inetAddress.getHostAddress()) ?
                                            inetAddress.getHostAddress() :
                                            inetAddress.getHostName() + " " + inetAddress.getHostAddress())
                            .collect(Collectors.joining(", "))

                            + "] port " + instance.getPort()

                            + (instance.getAttributes().size() > 0 ?
                            " attributes: " + instance.getAttributes().keySet().stream()
                                    .map(s -> s + " = " + instance.getAttributes().get(s))
                                    .collect(Collectors.joining(", "))
                            : "")
                    ));

                    logAndJoin(result, "");
                }
            }

        } catch (UnknownHostException e) {
            logAndJoin(result, "Unknown host: " + e.toString());
            logger.error("Unknown host: ", e);

        } catch (IOException e) {
            logAndJoin(result, "IO error: " + e.toString());
            logger.error("IO error: ", e);
        }


        model.put("info", String.join("<br/>", result));

        return modelAndView;
    }
}
