/*
 * Copyright 2013 Michael Chermside
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package webcomicreader.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import webcomicreader.webapp.storage.StorageFacade;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This controller is used for dumping and restoring the state
 * of the database.
 */
@Controller
public class BackupRestoreController {

    @Autowired
    private StorageFacade storage;

// FIXME: Remove this
//    /**
//     * Downloads the entire contents of the database in a json format.
//     */
//    @RequestMapping(value = "/dumpdb", method = RequestMethod.GET)
//    public String dumpDatabase(Model model)
//    {
//        String dumpdbStr = storage.dumpdb();
//        model.addAttribute("dumpdb", dumpdbStr);
//        return "dumpdb";
//    }


    /**
     * Downloads the entire contents of the database in a json format.
     */
    @RequestMapping(value = "/dumpdb", method = RequestMethod.GET)
    public void dumpDatabase(HttpServletResponse response) throws IOException {
        String dateStr = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date());
        String dumpdbStr = storage.dumpdb();
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=dumpdb_" + dateStr + ".json");
        response.setContentType("application/json");
        response.getWriter().write(dumpdbStr);
    }

}
