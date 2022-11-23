using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.DTOs;

namespace WebAPI.Models.ORM
{
    partial class Appointment
    {
        public static AppointmentDTO ToDTO(Appointment item, int level)
        {
            if (item == null || level <= 0)
            {
                return null;
            }

            return new AppointmentDTO
            {
                DateTimeStart = item.DateTimeStart,
                DateTimeEnd = item.DateTimeEnd,
                Shop = Shop.ToDTO(item.Shop, level - 1),
                DateCreated = item.DateCreated,
                DateDeleted = item.DateDeleted,
                DateModified = item.DateModified,
                Deleted = item.Deleted,
                Id = item.Id,
                ShopId = item.ShopId
            };
        }

        public static ICollection<AppointmentDTO> ToListDTO(ICollection<Appointment> list, int level)
        {
            if (list == null || level <= 0)
            {
                return null;
            }

            return list.Select(x => x.ToDTO(level)).ToList();
        }

        public AppointmentDTO ToDTO(int level)
        {
            return level > 0 ? new AppointmentDTO
            {
                DateTimeStart = DateTimeStart,
                DateTimeEnd = DateTimeEnd,
                Shop = Shop.ToDTO(Shop, level - 1),
                DateCreated = DateCreated,
                DateDeleted = DateDeleted,
                DateModified = DateModified,
                Deleted = Deleted,
                Id = Id,
                ShopId = ShopId
            } : null;
        }
    }

}